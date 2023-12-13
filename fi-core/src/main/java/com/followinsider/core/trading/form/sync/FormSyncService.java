package com.followinsider.core.trading.form.sync;

import com.followinsider.common.entities.sync.SyncProgress;
import com.followinsider.common.entities.sync.SyncStatus;
import com.followinsider.common.utils.ListUtils;
import com.followinsider.common.utils.StringUtils;
import com.followinsider.core.trading.form.*;
import com.followinsider.core.trading.quarter.Quarter;
import com.followinsider.core.trading.form.failed.FailedForm;
import com.followinsider.core.trading.form.failed.FailedFormRepository;
import com.followinsider.core.trading.quarter.QuarterService;
import com.followinsider.core.trading.quarter.QuarterUtils;
import com.followinsider.data.forms.refs.FormRefLoader;
import com.followinsider.data.forms.f345.OwnershipDocLoader;
import com.followinsider.data.forms.f345.OwnershipDoc;
import com.followinsider.data.forms.refs.FormRef;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static final int MAX_ERROR_LENGTH = 255;

    @Value("${trading.form_batch_size}")
    private int formBatchSize;

    public FormSyncProgress getProgress() {
        int formsTotal = formService.count();
        int formsFailed = (int) failedFormRepository.count();
        SyncProgress quarterProgress = quarterService.getSyncProgress();
        return new FormSyncProgress(formsTotal, formsFailed, quarterProgress);
    }

    @Async
    public void syncLastDays() {
        // TODO: Implement this.
    }
    
    @Async
    public void verifyQuarters() {
        // TODO: Implement this.
    }

    @Async
    @Transactional
    public void syncFailed() {
        List<FailedForm> failedForms = failedFormRepository.findAll();
        List<FormRef> refs = failedForms.stream().map(FailedForm::toRef).toList();
        failedFormRepository.deleteAll();
        safeSyncRefs(refs, "form_failed");
    }

    @Async
    public void syncByStatus(SyncStatus status) {
        List<Quarter> quarters = quarterService.findBy(status);
        quarters.sort(QuarterUtils.comparatorDesc());
        quarters.forEach(this::syncQuarter);
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
    public void syncCompany(String cik) {
        List<FormRef> refs = formRefLoader.loadByCik(cik);
        safeSyncRefs(refs, "CIK " + cik);
    }

    @Async
    public void syncQuarter(int yearVal, int quarterVal) {
        Quarter quarter = quarterService.findByOrCreate(yearVal, quarterVal);
        syncQuarter(quarter);
    }

    @Async
    public void syncQuarter(Quarter quarter) {
        if (quarter.getSyncStatus().isFull()) return;

        int yearVal = quarter.getYearVal();
        int quarterVal = quarter.getQuarterVal();

        List<FormRef> refs = formRefLoader.loadByQuarter(yearVal, quarterVal);
        quarter.setFormNum(refs.size());

        SyncStatus syncStatus = safeSyncRefs(refs, QuarterUtils.alias(quarter));
        quarter.setSyncStatus(syncStatus);

        quarterService.save(quarter);
    }

    public SyncStatus safeSyncRefs(List<FormRef> refs, String source) {
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
        List<List<FormRef>> batches = ListUtils.divideBySize(refs, formBatchSize);
        if (batches.size() == 1) return syncRefs(batches.get(0), source);

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

        List<Form> forms = ListUtils.filterType(results, Form.class);
        formPersistenceService.saveForms(forms, source);

        List<FailedForm> failedForms = ListUtils.filterType(results, FailedForm.class);

        if (!failedForms.isEmpty()) {
            log.warn("Failed loading {} forms :: count: {}", source, failedForms.size());
            failedFormRepository.saveAll(failedForms);
            return SyncStatus.PARTIAL;
        }

        return SyncStatus.FULL;
    }

    // Returns Form or FailedForm
    private Object loadByRef(FormRef ref) {
        try {
            OwnershipDoc doc = ownershipDocLoader.loadByRef(ref);
            return FormMapper.mapOwnershipDoc(doc);

        } catch (Exception e) {
            String error = formatError(e.getMessage());
            return new FailedForm(ref, error);
        }
    }

    private String formatError(String error) {
        return error != null
                ? StringUtils.handleOverflow(error, MAX_ERROR_LENGTH)
                : "Something went wrong";
    }

}
