package com.followinsider.modules.trading.form.loader;

import com.alexkouzel.client.exceptions.HttpRequestException;
import com.alexkouzel.common.exceptions.ParsingException;
import com.alexkouzel.filing.FilingType;
import com.alexkouzel.filing.reference.FilingReference;
import com.alexkouzel.filing.reference.FilingReferenceLoader;
import com.alexkouzel.filing.reference.FilingReferenceUtils;
import com.alexkouzel.filing.reference.latest.LatestFeedCount;
import com.alexkouzel.filing.type.f345.OwnershipDocument;
import com.alexkouzel.filing.type.f345.OwnershipDocumentLoader;
import com.followinsider.common.entities.TimeRange;
import com.followinsider.common.utils.ListUtils;
import com.followinsider.modules.trading.fiscalquarter.FiscalQuarterRepository;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarter;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterForms;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterRange;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterVals;
import com.followinsider.modules.trading.form.models.Form;
import com.followinsider.modules.trading.fiscalquarter.FiscalQuarterFormsRepository;
import com.followinsider.modules.trading.form.FormRepository;
import com.followinsider.modules.trading.form.converter.FormConverter;
import com.followinsider.modules.trading.form.saver.FormSaver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormLoaderService implements FormLoader {

    private final OwnershipDocumentLoader ownershipDocumentLoader;

    private final FilingReferenceLoader filingReferenceLoader;

    private final FormConverter formConverter;

    private final FormSaver formSaver;

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final FiscalQuarterFormsRepository fiscalQuarterFormsRepository;

    private final FormRepository formRepository;

    @Value("${sec.form_batch_size}")
    private int formBatchSize;

    @Override
    @Async
    public void loadLatest() {
        String source = "latest 100 F4";
        loadByRefLoader(source, () -> filingReferenceLoader
                .loadLatest(0, LatestFeedCount.HUNDRED, FilingType.F4));
    }

    @Override
    @Async
    public void loadLastDays(int days) {
        for (int day = 0; day < days; day++) {
            loadDaysAgo(day);
        }
    }

    private void loadDaysAgo(int daysAgo) {
        String source = daysAgo + " days ago";
        loadByRefLoader(source, () -> filingReferenceLoader.loadDaysAgo(daysAgo));
    }

    @Override
    @Async
    public void loadByCik(String cik) {
        String source = "cik " + cik;
        loadByRefLoader(source, () -> filingReferenceLoader.loadByCik(cik));
    }

    @Override
    @Async
    public void loadFiscalQuarterRange(String from, String to) {
        loadFiscalQuarterRange(new FiscalQuarterRange(from, to));
    }

    private void loadFiscalQuarterRange(FiscalQuarterRange range) {
        range.generate().forEach(vals -> loadFiscalQuarter(vals.year(), vals.quarter()));
    }

    @Override
    @Async
    public void loadFiscalQuarter(int year, int quarter) {
        FiscalQuarter fiscalQuarter = fiscalQuarterRepository.findByYearAndQuarter(year, quarter);

        if (fiscalQuarter == null) {
            String source = new FiscalQuarterVals(year, quarter).toAlias();
            logLoadingAborted(source, "invalid fiscal quarter");
            return;
        }
        loadFiscalQuarter(fiscalQuarter);
    }

    private void loadFiscalQuarter(FiscalQuarter fiscalQuarter) {
        FiscalQuarterForms forms = fiscalQuarterFormsRepository
                .findByFiscalQuarter(fiscalQuarter)
                .orElse(new FiscalQuarterForms(fiscalQuarter));

        loadFiscalQuarter(forms);
    }

    private void loadFiscalQuarter(FiscalQuarterForms forms) {
        FiscalQuarterVals vals = forms.getFiscalQuarter().getVals();
        String source = vals.toAlias();

        if (forms.isFull()) {
            logLoadingAborted(source, "already fully loaded");
            return;
        }
        loadByRefLoader(source,
                () -> filingReferenceLoader.loadByFiscalQuarter(vals.year(), vals.quarter()))
                .ifPresent((progress) -> updateFiscalQuarterForms(forms, progress));
    }

    private void updateFiscalQuarterForms(FiscalQuarterForms forms, FormLoaderProgress progress) {
        forms.setTotal(progress.total());
        forms.setLoaded(progress.loaded());
        fiscalQuarterFormsRepository.save(forms);
    }

    /* --------------------------------------------------- */
    /*                 FORM LOADER HELPERS                 */
    /* --------------------------------------------------- */

    @FunctionalInterface
    private interface RefLoader {
        List<FilingReference> loadRefs() throws ParsingException, HttpRequestException;
    }

    private Optional<FormLoaderProgress> loadByRefLoader(String source, RefLoader refLoader) {
        return loadRefs(source, refLoader)
                .flatMap(refs -> {
                    FormLoaderProgress progress = safeLoadByRefs(source, refs);
                    logLoadedForms(source, progress);
                    return Optional.of(progress);
                });
    }

    private Optional<List<FilingReference>> loadRefs(String source, RefLoader refLoader) {
        try {
            List<FilingReference> refs = refLoader.loadRefs();
            return Optional.of(refs);

        } catch (ParsingException | HttpRequestException e) {
            logLoadingFormRefsError(source, e.getMessage());
            return Optional.empty();
        }
    }

    private FormLoaderProgress safeLoadByRefs(String source, List<FilingReference> refs) {
        int total = refs.size();

        refs = FilingReferenceUtils.filterType(refs, FilingType.F4);
        refs = FilingReferenceUtils.removeDups(refs);
        refs = filterOldRefs(refs);

        if (refs.isEmpty())
            return new FormLoaderProgress(total, total, 0, 0);

        int loaded = loadByRefsInBatches(source, refs);
        int filtered = total - refs.size();

        return new FormLoaderProgress(total, filtered, loaded);
    }

    private int loadByRefsInBatches(String source, List<FilingReference> refs) {
        if (refs.size() <= formBatchSize)
            return loadByRefs(refs);

        List<List<FilingReference>> batches = ListUtils.splitBySize(refs, formBatchSize);

        int batchCount = batches.size();
        logLoadingStarted(source, refs.size(), batchCount);

        int loaded = 0;
        for (int i = 0; i < batches.size(); i++) {
            List<FilingReference> batch = batches.get(i);
            int batchLoaded = loadByRefs(batch);
            logLoadedFormBatch(source, i, batchCount, batch.size(), batchLoaded);
            loaded += batchLoaded;
        }
        return loaded;
    }

    private int loadByRefs(List<FilingReference> refs) {
        List<Form> forms = refs
                .parallelStream()
                .map(this::loadByRef)
                .filter(Objects::nonNull)
                .toList();

        formSaver.saveForms(forms);
        return forms.size();
    }

    private Form loadByRef(FilingReference ref) {
        try {
            OwnershipDocument doc = ownershipDocumentLoader.loadByRef(ref);
            return formConverter.convertToForm(doc);

        } catch (ParsingException | HttpRequestException e) {
            logLoadingFormError(ref.getTxtUrl(), e.getMessage());
            return null;
        }
    }

    /* --------------------------------------------------- */
    /*               FILTERING OLD REFERENCES              */
    /* --------------------------------------------------- */

    public List<FilingReference> filterOldRefs(List<FilingReference> refs) {
        if (ListUtils.isEmpty(refs)) return new ArrayList<>();

        LocalDate from = FilingReferenceUtils.getFirst(refs).filedAt();
        LocalDate to = FilingReferenceUtils.getLast(refs).filedAt();
        TimeRange timeRange = new TimeRange(from, to);

        return shouldFilterOneByOne(refs, timeRange)
                ? filterOldRefsOneByOne(refs)
                : filterOldRefsByTimeRange(refs, timeRange);
    }

    private boolean shouldFilterOneByOne(List<FilingReference> refs, TimeRange timeRange) {
        return (refs.size() < 5) || (refs.size() < 50 && timeRange.days() > 100);
    }

    private List<FilingReference> filterOldRefsByTimeRange(List<FilingReference> refs, TimeRange timeRange) {
        Set<String> accNos = formRepository.findIdsFiledBetween(timeRange.from(), timeRange.to());
        return FilingReferenceUtils.filterAccNos(refs, accNos);
    }

    private List<FilingReference> filterOldRefsOneByOne(List<FilingReference> refs) {
        return ListUtils.filter(refs, ref -> !formRepository.existsById(ref.accNo()));
    }

    /* --------------------------------------------------- */
    /*                   LOGGING HELPERS                   */
    /* --------------------------------------------------- */

    private void logLoadingAborted(String source, String reason) {
        log.warn("Aborted loading forms :: source: '{}', reason: '{}'", source, reason);
    }

    private void logLoadingStarted(String source, int totalForms, int batchCount) {
        log.info("Started loading forms :: source: '{}', total: {}, batches: {}", source, totalForms, batchCount);
    }

    private void logLoadedFormBatch(String source, int batchIdx, int batchCount, int batchSize, int loaded) {
        log.info("Loaded {}/{} form batch :: source: '{}', size: {}, loaded: {}",
                batchIdx, batchCount, source, batchSize, loaded);
    }

    private void logLoadedForms(String source, FormLoaderProgress progress) {
        log.info("Finished loading forms :: source: '{}', total: {}, filtered: {}, loaded: {}, failed: {}",
                source, progress.total(), progress.filtered(), progress.loaded(), progress.failed());
    }

    private void logLoadingFormRefsError(String source, String error) {
        log.error("Failed loading form refs :: source: '{}', error: '{}'", source, error);
    }

    private void logLoadingFormError(String url, String error) {
        log.error("Failed loading form :: url: '{}', error: '{}'", url, error);
    }

}
