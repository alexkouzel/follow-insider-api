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
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarter;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterRange;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterVals;
import com.followinsider.modules.trading.form.models.Form;
import com.followinsider.modules.trading.fiscalquarter.FiscalQuarterFormsRepository;
import com.followinsider.modules.trading.form.FormRepository;
import com.followinsider.modules.trading.form.converter.FormConverter;
import com.followinsider.modules.trading.form.saver.FormSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FormLoaderService implements FormLoader {

    private final OwnershipDocumentLoader ownershipDocumentLoader;

    private final FilingReferenceLoader filingReferenceLoader;

    private final FormConverter formConverter;

    private final FormSaver formSaver;

    private final FiscalQuarterFormsRepository fiscalQuarterFormsRepository;

    private final FormRepository formRepository;

    @Value("${sec.form_batch_size}")
    private int formBatchSize;

    @Override
    public Set<String> updateLatest(Set<String> accNos) {
        try {
            List<FilingReference> refs = filingReferenceLoader
                    .loadLatest(0, LatestFeedCount.HUNDRED, FilingType.F4);

            List<FilingReference> newRefs = FilingReferenceUtils.filterAccNos(refs, accNos);
            if (newRefs.isEmpty()) return accNos;

            loadByRefsInBatches(newRefs);
            return FilingReferenceUtils.getAccNos(refs);

        } catch (HttpRequestException | ParsingException e) {
            // TODO: Log the error.
            return null;
        }
    }

    @Override
    @Async
    public void loadLatest() {
        try {
            List<FilingReference> refs = filingReferenceLoader
                    .loadLatest(0, LatestFeedCount.HUNDRED, FilingType.F4);

            FormLoaderStatus loaderStatus = safeLoadByRefs(refs);

        } catch (HttpRequestException | ParsingException e) {
            // TODO: Log the error.
        }
    }

    @Override
    @Async
    public void loadLastDays(int days) {
        try {
            for (int day = 0; day < days; day++) {
                List<FilingReference> refs = filingReferenceLoader.loadDaysAgo(day);
                FormLoaderStatus loaderStatus = safeLoadByRefs(refs);
            }

        } catch (HttpRequestException | ParsingException e) {
            // TODO: Log the error.
        }
    }

    @Override
    @Async
    public void loadByCik(String cik) {
        try {
            List<FilingReference> refs = filingReferenceLoader.loadByCik(cik);
            FormLoaderStatus formLoaderStatus = safeLoadByRefs(refs);

        } catch (HttpRequestException e) {
            // TODO: Log the error.
        }
    }

    @Override
    @Async
    public void loadFiscalQuarter(int year, int quarter) {
        try {
            List<FilingReference> refs = filingReferenceLoader.loadByQuarter(year, quarter);
            FormLoaderStatus loaderStatus = safeLoadByRefs(refs);

        } catch (ParsingException | HttpRequestException e) {
            // TODO: Log the error.
        }
    }

    @Override
    @Async
    public void loadFiscalQuarter(FiscalQuarterVals vals) {
        loadFiscalQuarter(vals.year(), vals.quarter());
    }

    @Override
    @Async
    public void loadFiscalQuarter(FiscalQuarter fiscalQuarter) {
        loadFiscalQuarter(fiscalQuarter.getVals());
    }

    @Override
    @Async
    public void loadFiscalQuarterRange(String from, String to) {
        loadFiscalQuarterRange(new FiscalQuarterVals(from), new FiscalQuarterVals(to));
    }

    @Override
    @Async
    public void loadFiscalQuarterRange(FiscalQuarterVals from, FiscalQuarterVals to) {
        // TODO: Implement this.
    }

    @Override
    @Async
    public void loadFiscalQuarterRange(FiscalQuarterRange range) {
        loadFiscalQuarterRange(range.from(), range.to());
    }

    @Override
    @Async
    public void loadByLoaderStatus(FormLoaderStatus formLoaderStatus) {
        fiscalQuarterFormsRepository
                .findByLoaderStatus(formLoaderStatus)
                .forEach(this::loadFiscalQuarter);
    }

    private FormLoaderStatus safeLoadByRefs(List<FilingReference> refs) {
        refs = FilingReferenceUtils.removeDups(refs);
        refs = filterOldRefs(refs);

        if (refs.isEmpty()) return FormLoaderStatus.FULL;
        return loadByRefsInBatches(refs);
    }

    private FormLoaderStatus loadByRefsInBatches(List<FilingReference> refs) {
        if (refs.size() <= formBatchSize)
            return loadByRefs(refs);

        List<FormLoaderStatus> statuses = ListUtils
                .splitBySize(refs, formBatchSize)
                .stream()
                .map(this::loadByRefs)
                .toList();

        boolean full = statuses.stream().allMatch(status -> status == FormLoaderStatus.FULL);
        return full ? FormLoaderStatus.FULL : FormLoaderStatus.PARTIAL;
    }

    private FormLoaderStatus loadByRefs(List<FilingReference> refs) {
        List<Form> forms = refs
                .parallelStream()
                .map(this::loadByRef)
                .filter(Objects::nonNull)
                .toList();

        formSaver.saveForms(forms);

        boolean full = forms.size() == refs.size();
        return full ? FormLoaderStatus.FULL : FormLoaderStatus.PARTIAL;
    }

    private Form loadByRef(FilingReference ref) {
        try {
            OwnershipDocument doc = ownershipDocumentLoader.loadByRef(ref);
            return formConverter.convertToForm(doc);

        } catch (ParsingException | HttpRequestException e) {
            // TODO: Log the error.
            return null;
        }
    }

    public List<FilingReference> filterOldRefs(List<FilingReference> refs) {
        if (ListUtils.isEmpty(refs)) return new ArrayList<>();

        LocalDate from = FilingReferenceUtils.getFirst(refs).filedAt();
        LocalDate to = FilingReferenceUtils.getLast(refs).filedAt();

        TimeRange timeRange = new TimeRange(from, to);

        boolean oneByOne = (refs.size() < 10)
                || (refs.size() < 50 && timeRange.days() > 90);

        return oneByOne
                ? filterOldRefsOneByOne(refs)
                : filterOldRefsByTimeRange(refs, timeRange);
    }

    private List<FilingReference> filterOldRefsByTimeRange(List<FilingReference> refs, TimeRange timeRange) {
        Set<String> accNos = formRepository.findIdsFiledBetween(timeRange.from(), timeRange.to());
        return FilingReferenceUtils.filterAccNos(refs, accNos);
    }

    private List<FilingReference> filterOldRefsOneByOne(List<FilingReference> refs) {
        return ListUtils.filter(refs, ref -> !formRepository.existsById(ref.accNo()));
    }

}
