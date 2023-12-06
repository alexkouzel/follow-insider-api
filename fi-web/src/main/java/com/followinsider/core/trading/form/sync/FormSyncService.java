package com.followinsider.core.trading.form.sync;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Async
    public void syncFiscalQuarter(int year, int quarter) {
        FiscalQuarter fiscalQuarter = fiscalQuarterService.getByYearAndQuarter(year, quarter);
        syncFiscalQuarter(fiscalQuarter);
    }

    @Async
    public void syncFiscalQuarter(FiscalQuarter fiscalQuarter) {
        if (fiscalQuarter.getSyncStatus() == SyncStatus.FULL) return;

        int year = fiscalQuarter.getYearVal();
        int quarter = fiscalQuarter.getQuarterVal();

        List<FormRef> refs = formRefLoader.loadByQuarter(year, quarter);
        fiscalQuarter.setFormNum(refs.size());

        String source = FiscalQuarterUtils.getAlias(fiscalQuarter);
        SyncStatus status = syncRefs(refs, source);
        fiscalQuarter.setSyncStatus(status);

        fiscalQuarterService.save(fiscalQuarter);
    }

    @Async
    public void syncDaysAgo(int daysAgo) {
        syncRefs(formRefLoader.loadDaysAgo(daysAgo), daysAgo + " days ago");
    }

    @Async
    public void syncLatest(int count) {
        syncRefs(formRefLoader.loadLatest(0, 200), "latest " + count);
    }

    @Async
    public void syncCik(String cik) {
        syncRefs(formRefLoader.loadByCik(cik), "CIK " + cik);
    }

    private SyncStatus syncRefs(List<FormRef> refs, String source) {
        if (CollectionUtils.isEmpty(refs)) return SyncStatus.FULL;

        try {
            List<FormRef> newRefs = formService.filterOldRefs(refs);
            if (newRefs.isEmpty()) return SyncStatus.FULL;

            log.info("Synchronizing forms :: {} :: count: {}", source, newRefs.size());
            return unsafeSyncRefs(newRefs, source);

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
        failedFormRepository.saveAll(failedForms);

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

}
