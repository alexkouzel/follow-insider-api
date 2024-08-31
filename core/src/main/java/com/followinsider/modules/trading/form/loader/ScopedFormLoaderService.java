package com.followinsider.modules.trading.form.loader;

import com.alexkouzel.client.exceptions.HttpRequestException;
import com.alexkouzel.common.exceptions.ParsingException;
import com.alexkouzel.filing.FilingType;
import com.alexkouzel.filing.reference.FilingReference;
import com.alexkouzel.filing.reference.FilingReferenceLoader;
import com.alexkouzel.filing.reference.latest.LatestFeedCount;
import com.followinsider.modules.trading.fiscalquarter.FiscalQuarterService;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarter;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterForms;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterRange;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterVals;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScopedFormLoaderService implements ScopedFormLoader {

    private final FilingReferenceLoader filingReferenceLoader;

    private final FiscalQuarterService fiscalQuarterService;

    private final FormLoaderService formLoaderService;

    private final Set<String> scopes = new HashSet<>();

    private final Object scopeLock = new Object();

    @Override
    @Async
    public void loadLatest() {
        String scope = "latest 100 F4";

        FilingReferenceSupplier refSupplier = () -> filingReferenceLoader
            .loadLatest(0, LatestFeedCount.HUNDRED, FilingType.F4);

        loadScope(scope, refSupplier);
    }

    @Override
    @Async
    public void loadLastDays(int days) {
        for (int day = 0; day < days; day++) {
            loadDaysAgo(day);
        }
    }

    @Override
    @Async
    public void loadDaysAgo(int daysAgo) {
        String scope = daysAgo + " days ago";
        loadScope(scope, () -> filingReferenceLoader.loadDaysAgo(daysAgo, FilingType.F4));
    }

    @Override
    @Async
    public void loadByCompany(int cik) {
        String scope = "CIK " + cik;
        loadScope(scope, () -> filingReferenceLoader.loadByCik(cik));
    }

    @Override
    @Async
    public void loadFiscalQuarterRange(String from, String to) {
        FiscalQuarterRange range = new FiscalQuarterRange(from, to);
        range.generate().forEach(vals -> loadFiscalQuarter(vals.year(), vals.quarter()));
    }

    @Override
    @Async
    public void loadFiscalQuarter(int year, int quarter) {
        fiscalQuarterService
            .findByYearAndQuarter(year, quarter)
            .ifPresentOrElse(this::loadFiscalQuarter,
                () -> {
                    String source = new FiscalQuarterVals(year, quarter).toAlias();
                    logLoadingAborted(source, "invalid fiscal quarter");
                });
    }

    private void loadFiscalQuarter(FiscalQuarter fiscalQuarter) {
        FiscalQuarterForms forms = fiscalQuarterService.findFormsByFiscalQuarter(fiscalQuarter);
        FiscalQuarterVals vals = forms.getFiscalQuarter().getVals();

        String scope = vals.toAlias();

        FilingReferenceSupplier refSupplier = () -> filingReferenceLoader
            .loadByFiscalQuarter(vals.year(), vals.quarter());

        loadScope(scope, refSupplier)
            .ifPresent((progress) -> updateFiscalQuarterForms(forms, progress));
    }

    private void updateFiscalQuarterForms(FiscalQuarterForms forms, FormLoaderProgress progress) {
        forms.setTotal(progress.total());
        forms.setLoaded(progress.old() + progress.loaded());
        forms.setLastUpdated(LocalDate.now());
        fiscalQuarterService.saveForms(forms);
    }

    private Optional<FormLoaderProgress> loadScope(String scope, FilingReferenceSupplier refSupplier) {
        if (!lockScope(scope))
            return Optional.empty();

        try {
            List<FilingReference> refs = refSupplier.get();
            FormLoaderProgress progress = formLoaderService.load(scope, refs);
            return Optional.ofNullable(progress);

        } catch (ParsingException | HttpRequestException e) {
            logLoadingAborted(scope, e.getMessage());
            return Optional.empty();

        } finally {
            unlockScope(scope);
        }
    }

    private boolean lockScope(String scope) {
        synchronized (scopeLock) {
            if (scopes.contains(scope)) {
                logLoadingAborted(scope, "already loading");
                return false;
            }
            scopes.add(scope);
            return true;
        }
    }

    private void unlockScope(String scope) {
        synchronized (scopeLock) {
            scopes.remove(scope);
        }
    }

    private void logLoadingAborted(String scope, String reason) {
        log.warn("Aborted form loading :: scope: '{}', reason: '{}'", scope, reason);
    }

    @FunctionalInterface
    private interface FilingReferenceSupplier {
        List<FilingReference> get() throws HttpRequestException, ParsingException;
    }

}
