package com.followinsider.modules.trading.form.loader;

import com.alexkouzel.client.exceptions.HttpRequestException;
import com.alexkouzel.common.exceptions.ParsingException;
import com.alexkouzel.filing.FilingType;
import com.alexkouzel.filing.reference.FilingReference;
import com.alexkouzel.filing.reference.FilingReferenceUtils;
import com.alexkouzel.filing.type.f345.OwnershipDocument;
import com.alexkouzel.filing.type.f345.OwnershipDocumentLoader;
import com.followinsider.common.models.TimeRange;
import com.followinsider.common.utils.ListUtils;
import com.followinsider.modules.trading.form.FormRepository;
import com.followinsider.modules.trading.form.converter.FormConverter;
import com.followinsider.modules.trading.form.models.Form;
import com.followinsider.modules.trading.form.saver.FormSaver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormLoaderService {

    private final OwnershipDocumentLoader ownershipDocumentLoader;

    private final FormRepository formRepository;

    private final FormConverter formConverter;

    private final FormSaver formSaver;

    @Value("${edgar.form_batch_size}")
    private int formBatchSize;

    public FormLoaderProgress load(String scope, List<FilingReference> refs) {
        refs = FilingReferenceUtils.filterType(refs, FilingType.F4);
        refs = FilingReferenceUtils.removeDups(refs);

        int total = refs.size();

        List<FilingReference> newRefs = filterOld(refs);
        int old = total - newRefs.size();

        if (newRefs.isEmpty()) {
            logLoadingCancelled(scope, refs.size(), "already loaded");
            return new FormLoaderProgress(total, total, 0, 0);
        }

        int loaded = loadInBatches(scope, newRefs);
        int failed = total - loaded - old;

        return new FormLoaderProgress(total, old, loaded, failed);
    }

    private int loadInBatches(String scope, List<FilingReference> refs) {
        List<List<FilingReference>> batches = ListUtils.splitBySize(refs, formBatchSize);

        logLoadingStarted(scope, refs.size(), batches.size());

        int loadedTotal = 0;
        for (int batchIdx = 0; batchIdx < batches.size(); batchIdx++) {
            List<FilingReference> batch = batches.get(batchIdx);
            int loaded = loadBatch(batch);
            loadedTotal += loaded;

            logBatchLoaded(scope, batchIdx, batches.size(), batch.size(), loaded);
        }
        return loadedTotal;
    }

    private int loadBatch(List<FilingReference> refs) {
        List<Form> forms = refs
            .parallelStream()
            .map(this::fetch)
            .flatMap(Optional::stream)
            .toList();

        formSaver.saveForms(forms);
        return forms.size();
    }

    private Optional<Form> fetch(FilingReference ref) {
        try {
            OwnershipDocument doc = ownershipDocumentLoader.loadByRef(ref);
            return Optional.of(formConverter.convertToForm(doc));

        } catch (ParsingException | HttpRequestException e) {
            logLoadingError(ref.getTxtUrl(), e.getMessage());
            return Optional.empty();
        }
    }

    /* --------------------------------------------------- */
    /*                  FILTERING OLD REFS                 */
    /* --------------------------------------------------- */

    private List<FilingReference> filterOld(List<FilingReference> refs) {
        if (ListUtils.isEmpty(refs)) return new ArrayList<>();

        LocalDate from = FilingReferenceUtils.getFirst(refs).filedAt();
        LocalDate to = FilingReferenceUtils.getLast(refs).filedAt();

        TimeRange timeRange = new TimeRange(from, to);

        return refs.size() < 25
            ? filterOldByPresence(refs)
            : filterOldByTimeRange(refs, timeRange);
    }

    private List<FilingReference> filterOldByTimeRange(List<FilingReference> refs, TimeRange timeRange) {
        Set<String> oldAccNos = formRepository.findIdsFiledBetween(timeRange.from(), timeRange.to());
        return FilingReferenceUtils.filterAccNos(refs, oldAccNos);
    }

    private List<FilingReference> filterOldByPresence(List<FilingReference> refs) {
        Set<String> accNos = FilingReferenceUtils.getAccNos(refs);
        Set<String> oldAccNos = formRepository.findIdsPresentIn(accNos);
        return FilingReferenceUtils.filterAccNos(refs, oldAccNos);
    }

    /* --------------------------------------------------- */
    /*                   LOGGING HELPERS                   */
    /* --------------------------------------------------- */

    private void logLoadingCancelled(String scope, int total, String reason) {
        log.info("Loading cancelled :: scope: '{}', total: {}, reason: '{}'",
            scope, total, reason);
    }

    private void logLoadingStarted(String scope, int total, int batchCount) {
        log.info("Loading started :: scope: '{}', total: {}, batch_count: {}, batch_size: {}",
            scope, total, batchCount, formBatchSize);
    }

    private void logBatchLoaded(String scope, int batchIdx, int batchCount, int total, int loaded) {
        log.info("Batch loaded {}/{} :: scope: '{}', total: {}, loaded: {}",
            batchIdx + 1, batchCount, scope, total, loaded);
    }

    private void logLoadingError(String url, String error) {
        log.error("Loading error :: url: '{}', error: '{}'", url, error);
    }

}
