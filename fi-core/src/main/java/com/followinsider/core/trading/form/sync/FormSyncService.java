package com.followinsider.core.trading.form.sync;

import com.followinsider.common.SyncStatus;
import com.followinsider.common.entities.TimeRange;
import com.followinsider.common.utils.ListUtils;
import com.followinsider.common.utils.StringUtils;
import com.followinsider.core.trading.form.Form;
import com.followinsider.core.trading.form.FormService;
import com.followinsider.core.trading.form.failed.FailedForm;
import com.followinsider.core.trading.form.failed.FailedFormRepository;
import com.followinsider.core.trading.quarter.QuarterService;
import com.followinsider.core.trading.quarter.entities.Quarter;
import com.followinsider.core.trading.quarter.entities.QuarterVals;
import com.followinsider.core.trading.quarter.sync.QuarterSyncProgress;
import com.followinsider.secapi.forms.f345.OwnershipDoc;
import com.followinsider.secapi.forms.f345.OwnershipDocLoader;
import com.followinsider.secapi.forms.refs.FormRef;
import com.followinsider.secapi.forms.refs.FormRefLoader;
import com.followinsider.secapi.forms.refs.FormRefUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormSyncService {

    private final FailedFormRepository failedFormRepository;

    private final FormPersistenceService formPersistenceService;

    private final QuarterService quarterService;

    private final FormService formService;

    private final OwnershipDocLoader ownershipDocLoader;

    private final FormRefLoader formRefLoader;

    private static final int MAX_ERROR_LENGTH = 255;

    @Value("${trading.form_batch_size}")
    private int formBatchSize;

    public FormSyncProgress getProgress() {
        int formsTotal = formService.countAll();
        int formsFailed = (int) failedFormRepository.count();
        QuarterSyncProgress quarterProgress = quarterService.getSyncProgress();
        return new FormSyncProgress(formsTotal, formsFailed, quarterProgress);
    }

    @Async
    public void verify() {
        List<Quarter> quarters = quarterService.findBy(SyncStatus.FULL);
        sortQuartersDesc(quarters);
        quarters.forEach(this::verifyQuarter);
    }

    @Async
    public void verifyQuarter(Quarter quarter) {
        List<FormRef> refs = formRefLoader.loadByQuarter(quarter.getYear(), quarter.getQuarter());
        refs = FormRefUtils.removeDups(refs);
        TimeRange timeRange = FormRefUtils.getTimeRange(refs);

        int actual = formService.countBetween(timeRange);
        int expected = refs.size() - quarter.getInvalidFormNum();

        if (actual == expected) {
            quarter.setSyncStatus(SyncStatus.VERIFIED);
            quarterService.save(quarter);

            log.info("Successfully verified {} :: count: {}",
                    quarter.getAlias(), quarter.getFormNum());
        } else {
            log.warn("Failed verifying {} :: expected: {}, actual: {}",
                    quarter.getAlias(), expected, actual);
        }
    }

    @Async
    @Transactional
    public void syncFailed() {
        List<FailedForm> failedForms = failedFormRepository.findAll();
        failedFormRepository.deleteAll();

        List<FormRef> refs = ListUtils.map(failedForms, FailedForm::getRef);
        safeSyncRefs(refs, "form_failed");
    }

    @Async
    public void syncByStatus(SyncStatus status) {
        List<Quarter> quarters = quarterService.findBy(status);
        sortQuartersDesc(quarters);
        quarters.forEach(this::syncQuarter);
    }

    @Async
    public void syncLastDays(int days) {
        for (int i = 0; i < days; i++) {
            syncDaysAgo(i);
        }
    }

    @Async
    public void syncDaysAgo(int daysAgo) {
        syncDaysAgo(daysAgo, daysAgo + " days ago");
    }

    @Async
    public void syncDaysAgo(int daysAgo, String source) {
        List<FormRef> refs = formRefLoader.loadDaysAgo(daysAgo);
        safeSyncRefs(refs, source);
    }

    @Async
    public void syncLatest(int count) {
        List<FormRef> refs = formRefLoader.loadLatest(0, count);
        safeSyncRefs(refs, "latest " + count);
    }

    @Async
    public void syncCompany(String cik) {
        List<FormRef> refs = formRefLoader.loadByCik(cik);
        safeSyncRefs(refs, "CIK " + cik);
    }

    @Async
    public void syncQuarter(String alias) {
        QuarterVals vals = new QuarterVals(alias);
        syncQuarter(quarterService.findOrSaveBy(vals));
    }

    @Async
    public void syncQuarter(Quarter quarter) {
        if (quarter.getSyncStatus().isFull()) {
            log.warn("Quarter {} is already fully synchronized", quarter.getAlias());
            return;
        }
        QuarterVals vals = quarter.getVals();
        List<FormRef> refs = formRefLoader.loadByQuarter(vals.year(), vals.quarter());
        SyncStatus syncStatus = safeSyncRefs(refs, vals.getAlias());

        quarter.setSyncStatus(syncStatus);
        quarter.setFormNum(refs.size());
        quarterService.save(quarter);
    }

    public SyncStatus safeSyncRefs(List<FormRef> refs, String source) {
        refs = FormRefUtils.removeDups(refs);
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
        if (refs.size() <= formBatchSize) return syncRefs(refs, source);

        List<List<FormRef>> batches = ListUtils.splitBySize(refs, formBatchSize);
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
        if (failedForms.isEmpty()) return SyncStatus.FULL;

        log.warn("Failed loading {} forms :: count: {}", source, failedForms.size());
        failedFormRepository.saveAll(failedForms);
        return SyncStatus.PARTIAL;
    }

    /* Returns Form or FailedForm */
    private Object loadByRef(FormRef ref) {
        try {
            OwnershipDoc doc = ownershipDocLoader.loadByRef(ref);
            return FormOwnershipDocMapper.apply(doc);

        } catch (Exception e) {
            String error = formatError(e.getMessage());
            return new FailedForm(ref, error);
        }
    }

    private String formatError(String error) {
        return error != null
                ? StringUtils.overflow(error, MAX_ERROR_LENGTH)
                : "Something went wrong";
    }

    private void sortQuartersDesc(List<Quarter> quarters) {
        quarters.sort(Comparator
                .comparingInt(Quarter::getYear)
                .thenComparingInt(Quarter::getQuarter)
                .reversed());
    }

}
