package com.followinsider.core.trading.form.sync;

import com.followinsider.common.entity.ExecMode;
import com.followinsider.common.util.CollectionUtils;
import com.followinsider.core.trading.form.*;
import com.followinsider.core.trading.quarter.FiscalQuarter;
import com.followinsider.core.trading.form.failed.FailedForm;
import com.followinsider.core.trading.form.failed.FailedFormRepository;
import com.followinsider.core.trading.quarter.FiscalQuarterService;
import com.followinsider.core.trading.quarter.FiscalQuarterUtils;
import com.followinsider.loader.FormRefLoader;
import com.followinsider.loader.OwnershipDocLoader;
import com.followinsider.parser.f345.OwnershipDoc;
import com.followinsider.parser.ref.FormRef;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormSyncService {

    private final OwnershipDocLoader ownershipDocLoader;

    private final FailedFormRepository failedFormRepository;

    private final FiscalQuarterService fiscalQuarterService;

    private final FormPersistenceService formPersistenceService;

    private final FormRefLoader formRefLoader;

    private final FormService formService;

    @Value("${app.form_bunch_size}")
    private int formBunchSize;

    public void syncDaysAgo(int daysAgo, ExecMode execMode) {
        List<FormRef> refs = formRefLoader.loadDaysAgo(daysAgo);
        syncRefs(refs, daysAgo + " days ago", execMode);
    }

    public void syncLatest(int count, ExecMode execMode) {
        List<FormRef> refs = formRefLoader.loadLatest(0, 200);
        syncRefs(refs, "latest " + count, execMode);
    }

    public void syncCik(String cik, ExecMode execMode) {
        List<FormRef> refs = formRefLoader.loadByCik(cik);
        syncRefs(refs, "CIK " + cik, execMode);
    }

    public void syncFiscalQuarter(int year, int quarter, ExecMode execMode) {
        execute(execMode, () -> {
            FiscalQuarter fiscalQuarter = fiscalQuarterService.getByYearAndQuarter(year, quarter);
            syncFiscalQuarter(fiscalQuarter);
        });
    }

    public void syncFiscalQuarter(FiscalQuarter fiscalQuarter, ExecMode execMode) {
        if (fiscalQuarter.getSyncStatus() == SyncStatus.FULL) return;
        execute(execMode, () -> syncFiscalQuarter(fiscalQuarter));
    }

    private void syncFiscalQuarter(FiscalQuarter fiscalQuarter) {
        int year = fiscalQuarter.getYearVal();
        int quarter = fiscalQuarter.getQuarterVal();

        List<FormRef> refs = formRefLoader.loadByQuarter(year, quarter);
        fiscalQuarter.setFormNum(refs.size());

        String source = FiscalQuarterUtils.getAlias(fiscalQuarter);
        SyncStatus status = syncRefs(refs, source);
        fiscalQuarter.setSyncStatus(status);

        fiscalQuarterService.save(fiscalQuarter);
    }

    private void syncRefs(List<FormRef> refs, String source, ExecMode execMode) {
        execute(execMode, () -> syncRefs(refs, source));
    }

    private SyncStatus syncRefs(List<FormRef> refs, String source) {
        if (CollectionUtils.isEmpty(refs)) return SyncStatus.FULL;

        try {
            List<FormRef> newRefs = formService.filterOldRefs(refs);
            if (newRefs.isEmpty()) return SyncStatus.FULL;

            log.info("Synchronizing forms :: {} :: count: {}", source, newRefs.size());

            List<SyncStatus> statuses = new ArrayList<>();
            List<List<FormRef>> refBunches = CollectionUtils.divideBySize(newRefs, formBunchSize);

            for (int i = 0; i < refBunches.size(); i++) {
                String bunchSource = String.format("%s %d/%d", source, i + 1, refBunches.size());
                SyncStatus status = unsafeSyncRefs(refBunches.get(i), bunchSource);
                statuses.add(status);
            }
            return statuses.stream()
                    .allMatch(status -> status == SyncStatus.FULL)
                    ? SyncStatus.FULL : SyncStatus.PARTIAL;

        } catch (Exception e) {
            log.error("Failed synchronizing form :: {}", e.getMessage());
            return SyncStatus.FAILED;
        }
    }

    private SyncStatus unsafeSyncRefs(List<FormRef> refs, String source) {
        List<FailedForm> failedForms = Collections.synchronizedList(new ArrayList<>());

        List<Form> forms = refs
                .parallelStream()
                .map(ref -> loadByRef(ref, failedForms))
                .filter(Objects::nonNull)
                .toList();

        formPersistenceService.saveForms(forms, source);

        if (!failedForms.isEmpty()) {
            failedFormRepository.saveAll(failedForms);
            log.warn("Failed forms :: {} :: count: {}", source, failedForms.size());
        }

        return failedForms.isEmpty()
                ? SyncStatus.FULL
                : SyncStatus.PARTIAL;
    }

    private Form loadByRef(FormRef ref, List<FailedForm> failedForms) {
        try {
            OwnershipDoc doc = ownershipDocLoader.loadByRef(ref);
            return FormOwnershipDocMapper.map(doc);

        } catch (Exception e) {
            FailedForm failedForm = new FailedForm(ref, e.getMessage());
            failedForms.add(failedForm);
        }
        return null;
    }

    private void execute(ExecMode execMode, Runnable runnable) {
        if (execMode == ExecMode.SYNC) {
            runnable.run();
        } else {
            CompletableFuture.runAsync(runnable);
        }
    }

}
