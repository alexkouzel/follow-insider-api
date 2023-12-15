package com.followinsider.core.trading.form.sync;

import com.followinsider.common.entities.TimeRange;
import com.followinsider.core.trading.quarter.sync.QuarterSyncProgress;
import com.followinsider.core.trading.quarter.sync.QuarterSyncStatus;
import com.followinsider.common.utils.ListUtils;
import com.followinsider.common.utils.StringUtils;
import com.followinsider.core.trading.form.Form;
import com.followinsider.core.trading.quarter.entities.Quarter;
import com.followinsider.core.trading.quarter.entities.QuarterVals;
import com.followinsider.core.trading.form.FormService;
import com.followinsider.core.trading.form.failed.FailedForm;
import com.followinsider.core.trading.form.failed.FailedFormRepository;
import com.followinsider.core.trading.quarter.QuarterService;
import com.followinsider.secapi.forms.refs.FormRefLoader;
import com.followinsider.secapi.forms.f345.OwnershipDocLoader;
import com.followinsider.secapi.forms.f345.OwnershipDoc;
import com.followinsider.secapi.forms.refs.FormRef;
import com.followinsider.secapi.forms.refs.FormRefUtils;
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
        FormSyncProgress progress = new FormSyncProgress(formsTotal, formsFailed, quarterProgress);
        log.info("Current form sync progress :: {}", progress);
        return progress;
    }

    @Async
    public void verifyFullQuarters() {
        log.info("Started verifying full quarters");
        List<Quarter> quarters = quarterService.findBy(QuarterSyncStatus.FULL);
        quarters.forEach(this::verifyQuarter);
    }

    @Async
    public void verifyQuarter(String alias) {
        Quarter quarter = quarterService.findOrSaveBy(new QuarterVals(alias));
        verifyQuarter(quarter);
    }

    @Async
    public void verifyQuarter(Quarter quarter) {
        String alias = quarter.getAlias();

        if (quarter.getSyncStatus() != QuarterSyncStatus.FULL) {
            log.warn("Failed verifying {} :: Sync status should be full", alias);
            return;
        }
        List<FormRef> refs = formRefLoader
                .loadByQuarter(quarter.getYear(), quarter.getQuarter());

        if (verifyRefs(refs, alias)) {
            quarter.setSyncStatus(QuarterSyncStatus.VERIFIED);
            quarterService.save(quarter);

            log.info("Successfully verified {} :: count: {}",
                    alias, quarter.getFormNum());
        }
    }

    private boolean verifyRefs(List<FormRef> refs, String source) {
        TimeRange timeRange = FormRefUtils.getTimeRange(refs);

        int actual = formService.countBetween(timeRange);
        int expected = refs.size();

        boolean verified = expected == actual;

        if (!verified)
            log.warn("Failed verifying {} :: expected: {}, actual: {}", source, expected, actual);

        return verified;
    }

    @Async
    @Transactional
    public void syncFailed() {
        List<FailedForm> failedForms = failedFormRepository.findAll();
        failedFormRepository.deleteAll();

        log.info("Synchronizing failed forms :: count: {}", failedForms.size());
        List<FormRef> refs = failedForms.stream().map(FailedForm::getRef).toList();
        safeSyncRefs(refs, "form_failed");
    }

    @Async
    public void syncByStatus(QuarterSyncStatus status) {
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
        QuarterVals vals = quarter.getVals();
        List<FormRef> refs = formRefLoader
                .loadByQuarter(vals.year(), vals.quarter());

        String source = vals.getAlias();
        Integer formNum = quarter.getFormNum();

        boolean noNewForms = formNum != null && formNum == refs.size();
        boolean fullSync = quarter.getSyncStatus() == QuarterSyncStatus.FULL;

        if (fullSync && noNewForms) {
            log.warn("Quarter {} is already fully synchronized", source);
            return;
        }
        if (formNum != null && formNum != refs.size()) {
            log.warn("Number of forms changed for quarter {}", source);
        }
        QuarterSyncStatus syncStatus = safeSyncRefs(refs, source);
        quarter.setSyncStatus(syncStatus);
        quarter.setFormNum(refs.size());
        quarterService.save(quarter);
    }

    public QuarterSyncStatus safeSyncRefs(List<FormRef> refs, String source) {
        refs = FormRefUtils.removeDups(refs);
        try {
            List<FormRef> newRefs = formService.filterOldRefs(refs);
            if (newRefs.isEmpty()) return QuarterSyncStatus.FULL;

            log.info("Started synchronizing {} :: count: {}", source, newRefs.size());
            return syncRefsInBatches(newRefs, source);

        } catch (Exception e) {
            log.error("Failed synchronizing {} :: {}", source, e.getMessage());
            return QuarterSyncStatus.FAILED;
        }
    }

    private QuarterSyncStatus syncRefsInBatches(List<FormRef> refs, String source) {
        if (refs.size() <= formBatchSize) return syncRefs(refs, source);

        List<List<FormRef>> batches = ListUtils.splitBySize(refs, formBatchSize);
        List<QuarterSyncStatus> statuses = new ArrayList<>();

        for (int i = 0; i < batches.size(); i++) {
            String batchSource = String.format("%s %d/%d", source, i + 1, batches.size());
            QuarterSyncStatus status = syncRefs(batches.get(i), batchSource);
            statuses.add(status);
        }
        boolean allFull = statuses.stream().allMatch(status -> status == QuarterSyncStatus.FULL);
        return allFull ? QuarterSyncStatus.FULL : QuarterSyncStatus.PARTIAL;
    }

    private QuarterSyncStatus syncRefs(List<FormRef> refs, String source) {
        List<Object> results = refs.parallelStream().map(this::loadByRef).toList();

        List<Form> forms = ListUtils.filterType(results, Form.class);
        formPersistenceService.saveForms(forms, source);

        List<FailedForm> failedForms = ListUtils.filterType(results, FailedForm.class);
        if (failedForms.isEmpty()) return QuarterSyncStatus.FULL;

        log.warn("Failed loading {} forms :: count: {}", source, failedForms.size());
        failedFormRepository.saveAll(failedForms);
        return QuarterSyncStatus.PARTIAL;
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
