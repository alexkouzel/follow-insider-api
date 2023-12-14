package com.followinsider.core.trading.form.sync;

import com.followinsider.common.entities.TimeRange;
import com.followinsider.common.entities.sync.SyncProgress;
import com.followinsider.common.entities.sync.SyncStatus;
import com.followinsider.common.utils.ListUtils;
import com.followinsider.common.utils.StringUtils;
import com.followinsider.core.trading.form.*;
import com.followinsider.core.trading.quarter.*;
import com.followinsider.core.trading.form.failed.FailedForm;
import com.followinsider.core.trading.form.failed.FailedFormRepository;
import com.followinsider.core.trading.quarter.entities.Quarter;
import com.followinsider.core.trading.quarter.entities.QuarterRange;
import com.followinsider.core.trading.quarter.entities.QuarterVals;
import com.followinsider.forms.refs.FormRefLoader;
import com.followinsider.forms.f345.OwnershipDocLoader;
import com.followinsider.forms.f345.OwnershipDoc;
import com.followinsider.forms.refs.FormRef;
import com.followinsider.forms.refs.FormRefUtils;
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
    public void verifyQuarters(String from, String to) {
        System.out.println(from);
        System.out.println(to);
        verifyQuarters(new QuarterRange(from, to));
    }

    @Async
    public void verifyQuarters(QuarterRange range) {
        List<Quarter> quarters = quarterService.findOrSaveBy(range.generate());
        sortQuartersDesc(quarters);
        for (Quarter quarter : quarters) {
            verifyQuarter(quarter);
        }
    }

    @Async
    public void verifyQuarter(Quarter quarter) {
        if (isQuarterFull(quarter)) {
            quarter.setSyncStatus(SyncStatus.VERIFIED);
            quarterService.save(quarter);
        } else {
            String source = quarter.getVals().getAlias();
            log.warn("Failed to verify quarter {}", source);
        }
    }

    @Async
    @Transactional
    public void syncFailed() {
        List<FailedForm> failedForms = failedFormRepository.findAll();
        List<FormRef> refs = failedForms.stream().map(FailedForm::getRef).toList();
        failedFormRepository.deleteAll();
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
        for (int i = 0; i < days; i++) syncDaysAgo(i);
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
        boolean fullSync = quarter.getSyncStatus() == SyncStatus.FULL;

        if (fullSync && noNewForms) {
            log.warn("Quarter {} is already fully synchronized", source);
            return;
        }
        if (formNum != null && formNum != refs.size()) {
            log.warn("Number of forms changed for quarter {}", source);
        }
        SyncStatus syncStatus = safeSyncRefs(refs, source);
        quarter.setSyncStatus(syncStatus);
        quarter.setFormNum(refs.size());
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
        if (refs.size() <= formBatchSize) return syncRefs(refs, source);

        List<List<FormRef>> batches = ListUtils.divideBySize(refs, formBatchSize);
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

    private boolean isQuarterFull(Quarter quarter) {
        List<FormRef> refs = formRefLoader.loadByQuarter(
                quarter.getYear(), quarter.getQuarter());

        TimeRange timeRange = FormRefUtils.getTimeRange(refs);
        int expectedRefSize = formService.countBetween(timeRange);
        return expectedRefSize == refs.size();
    }

    private void sortQuartersDesc(List<Quarter> quarters) {
        quarters.sort(Comparator
                .comparingInt(Quarter::getYear)
                .thenComparingInt(Quarter::getQuarter)
                .reversed());
    }

    private String formatError(String error) {
        return error != null
                ? StringUtils.handleOverflow(error, MAX_ERROR_LENGTH)
                : "Something went wrong";
    }

}
