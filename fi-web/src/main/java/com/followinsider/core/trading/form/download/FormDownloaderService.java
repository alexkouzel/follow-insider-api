package com.followinsider.core.trading.form.download;

import com.followinsider.core.trading.form.*;
import com.followinsider.core.trading.form.download.auto.FiscalQuarter;
import com.followinsider.core.trading.form.download.auto.FiscalQuarterRepository;
import com.followinsider.core.trading.form.download.failed.FailedFormRef;
import com.followinsider.core.trading.form.download.failed.FailedFormRefRepository;
import com.followinsider.loader.FormRefLoader;
import com.followinsider.loader.OwnershipDocLoader;
import com.followinsider.parser.f345.OwnershipDoc;
import com.followinsider.parser.ref.FormRef;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormDownloaderService {

    private final FailedFormRefRepository failedFormRefRepository;

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final OwnershipDocLoader ownershipDocLoader;

    private final FormSaverService formSaverService;

    private final FormRefLoader formRefLoader;

    private final FormService formService;

    @Async
    public void downloadQuarter(int year, int quarter) {
        FiscalQuarter fiscalQuarter = fiscalQuarterRepository.findByYearValAndQuarterVal(year, quarter)
                .orElseThrow(() -> new IllegalArgumentException("Invalid year and/or quarter"));

        downloadQuarter(fiscalQuarter);
    }

    @Async
    public void downloadQuarter(FiscalQuarter fiscalQuarter) {
        String source = fiscalQuarter.getAlias();
        Boolean downloaded = fiscalQuarter.getDownloaded();

        if (downloaded != null && downloaded) {
            String error = "fiscal quarter is already downloaded";
            log.info("Rejected form request :: source: {}, error: {}", source, error);
            return;
        }

        try {
            log.info("Received form request :: source: {}", source);
            fiscalQuarter.setDownloaded(false);

            int year = fiscalQuarter.getYearVal();
            int quarter = fiscalQuarter.getQuarterVal();

            List<FormRef> refs = formRefLoader.loadByQuarter(year, quarter);
            fiscalQuarter.setFormNum(refs.size());

            // Download forms by references
            downloadByRefs(source, refs);
            fiscalQuarter.setDownloaded(true);

        } finally {
            fiscalQuarterRepository.save(fiscalQuarter);
        }
    }

    @Async
    public void downloadDaysAgo(int daysAgo) {
        downloadByRefs(daysAgo + " days ago", () -> formRefLoader.loadDaysAgo(daysAgo));
    }

    @Async
    public void downloadLatest(int count) {
        downloadByRefs("latest " + count, () -> formRefLoader.loadLatest(0, 200));
    }

    @Async
    public void downloadCik(String cik) {
        downloadByRefs("CIK " + cik, () -> formRefLoader.loadByCik(cik));
    }

    private void downloadByRefs(String source, Supplier<List<FormRef>> refSuppler) {
        log.info("Received form request :: source: {}", source);
        try {
            downloadByRefs(source, refSuppler.get());
        } catch (RuntimeException e) {
            log.error("Failed form request :: {}", e.getMessage());
        }
    }

    private void downloadByRefs(String source, List<FormRef> refs) {
        if (refs == null || refs.isEmpty()) return;
        log.info("Loading forms by refs :: source: {}, count: {}", source, refs.size());

        List<FormRef> newRefs = formService.filterOldRefs(refs);
        if (newRefs.isEmpty()) return;

        List<Form> forms = loadByRefs(newRefs);
        handleFailedRefs(forms, refs);

        formSaverService.saveForms(forms, source);
    }

    private void handleFailedRefs(List<Form> loadedForms, List<FormRef> initialRefs) {
        if (initialRefs.size() == loadedForms.size()) return;

        Set<String> loadedIds = loadedForms.stream()
                .map(Form::getAccNum)
                .collect(Collectors.toSet());

        Set<String> savedIds = failedFormRefRepository.findAllIds();

        List<FailedFormRef> failedRefs = initialRefs.stream()
                .filter(ref -> !loadedIds.contains(ref.getAccNum()) && !savedIds.contains(ref.getAccNum()))
                .map(FailedFormRef::new)
                .toList();

        if (!failedRefs.isEmpty()) {
            failedFormRefRepository.saveAll(failedRefs);
        }
    }

    private List<Form> loadByRefs(List<FormRef> refs) {
        return refs.parallelStream()
                .map(ref -> {
                    try {
                        OwnershipDoc doc = ownershipDocLoader.loadByRef(ref);
                        return FormOwnershipDocMapper.map(doc);
                    } catch (ParseException | IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

}
