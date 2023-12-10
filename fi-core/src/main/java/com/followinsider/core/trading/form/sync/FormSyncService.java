package com.followinsider.core.trading.form.sync;

import com.followinsider.common.entities.sync.SyncProgress;
import com.followinsider.common.entities.sync.SyncStatus;
import com.followinsider.common.utils.CollectionUtils;
import com.followinsider.common.utils.StringUtils;
import com.followinsider.core.trading.form.*;
import com.followinsider.core.trading.quarter.Quarter;
import com.followinsider.core.trading.form.failed.FailedForm;
import com.followinsider.core.trading.form.failed.FailedFormRepository;
import com.followinsider.core.trading.quarter.QuarterService;
import com.followinsider.loaders.FormRefLoader;
import com.followinsider.loaders.OwnershipDocLoader;
import com.followinsider.parsing.f345.OwnershipDoc;
import com.followinsider.parsing.refs.FormRef;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormSyncService {

    private final OwnershipDocLoader ownershipDocLoader;

    private final FailedFormRepository failedFormRepository;

    private final QuarterService quarterService;

    private final FormPersistenceService formPersistenceService;

    private final FormRefLoader formRefLoader;

    private final FormService formService;

    @Value("${trading.form_bunch_size}")
    private int formBatchSize;

    public FormSyncProgress getProgress() {
        int formsTotal = formService.count();
        int formsFailed = (int) failedFormRepository.count();
        SyncProgress quarterProgress = quarterService.getSyncProgress();
        return new FormSyncProgress(formsTotal, formsFailed, quarterProgress);
    }

    @Async
    public void syncFailed() {
        List<FailedForm> failedForms = failedFormRepository.findAll();
        List<FormRef> refs = failedForms.stream().map(FailedForm::toRef).toList();
        syncRefs(refs, "form_failed");
    }

    @Async
    public void syncByStatus(String statusVal) {
        try {
            SyncStatus status = SyncStatus.valueOf(statusVal);
            syncByStatus(status);

        } catch (IllegalArgumentException e) {
            log.error("Invalid sync status :: {}", statusVal);
        }
    }

    @Async
    public void syncByStatus(SyncStatus status) {
        quarterService
                .getBySyncStatus(status)
                .forEach(this::syncQuarter);
    }

    @Async
    public void syncDaysAgo(int daysAgo) {
        List<FormRef> refs = formRefLoader.loadDaysAgo(daysAgo);
        safeSyncRefs(refs, daysAgo + " days ago");
    }

    @Async
    public void syncLatest(int count) {
        List<FormRef> refs = formRefLoader.loadLatest(0, 200);
        safeSyncRefs(refs, "latest " + count);
    }

    @Async
    public void syncCik(String cik) {
        List<FormRef> refs = formRefLoader.loadByCik(cik);
        safeSyncRefs(refs, "CIK " + cik);
    }

    @Async
    public void syncYear(int yearVal) {
        quarterService.getByYear(yearVal).forEach(this::syncQuarter);
    }

    @Async
    public void syncQuarter(int yearVal, int quarterVal) {
        Quarter quarter = quarterService.getByYearAndQuarter(yearVal, quarterVal);
        syncQuarter(quarter);
    }

    @Async
    public void syncQuarter(Quarter quarter) {
        if (quarter.getSyncStatus() == SyncStatus.FULL) return;

        int yearVal = quarter.getYearVal();
        int quarterVal = quarter.getQuarterVal();

        List<FormRef> refs = formRefLoader.loadByQuarter(yearVal, quarterVal);
        quarter.setFormNum(refs.size());

        String source = yearVal + "Q" + quarterVal;
        SyncStatus syncStatus = safeSyncRefs(refs, source);
        quarter.setSyncStatus(syncStatus);

        quarterService.save(quarter);
    }

    private SyncStatus safeSyncRefs(List<FormRef> refs, String source) {
        try {
            List<FormRef> newRefs = formService.filterOldRefs(refs);
            if (newRefs.isEmpty()) return SyncStatus.FULL;

            log.info("Started synchronizing {} :: count: {}", source, newRefs.size());
            return syncRefsInBatches(newRefs, source);

        } catch (Exception e) {
            log.error("Failed synchronizing {} :: {}", source, e.getMessage());
            return SyncStatus.FAILED;
        }
    }

    private SyncStatus syncRefsInBatches(List<FormRef> refs, String source) {
        List<List<FormRef>> batches = CollectionUtils.divideBySize(refs, formBatchSize);
        List<SyncStatus> statuses = new ArrayList<>();

        for (int i = 0; i < batches.size(); i++) {
            String batchSource = String.format("%s %d/%d", source, i + 1, batches.size());
            SyncStatus status = syncRefs(batches.get(i), batchSource);
            statuses.add(status);
        }
        boolean allFull = statuses.stream().allMatch(status -> status == SyncStatus.FULL);
        return allFull ? SyncStatus.FULL : SyncStatus.PARTIAL;
    }

    private SyncStatus syncRefs(List<FormRef> refs, String source) {
        List<Object> results = refs.parallelStream().map(this::loadByRef).toList();

        List<Form> forms = CollectionUtils.filterClass(results, Form.class);
        formPersistenceService.saveForms(forms, source);

        List<FailedForm> failedForms = CollectionUtils.filterClass(results, FailedForm.class);
        if (failedForms.isEmpty()) return SyncStatus.FULL;

        log.warn("Failed loading {} forms :: count: {}", source, failedForms.size());
        failedFormRepository.saveAll(failedForms);
        return SyncStatus.PARTIAL;
    }

    private Object loadByRef(FormRef ref) {
        try {
            OwnershipDoc doc = ownershipDocLoader.loadByRef(ref);
            return FormOwnershipDocMapper.map(doc);

        } catch (Exception e) {
            String error = formatError(e.getMessage());
            return new FailedForm(ref, error);
        }
    }

    private String formatError(String error) {
        if (error == null) return "Something went wrong";
        return StringUtils.overflow(error, 255);
    }

}
