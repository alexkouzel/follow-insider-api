package com.followinsider.core.service;

import com.followinsider.client.EdgarClient;
import com.followinsider.core.entity.*;
import com.followinsider.core.parser.InsiderFormParser;
import com.followinsider.core.repository.*;
import com.followinsider.loader.FormRefLoader;
import com.followinsider.loader.OwnershipDocLoader;
import com.followinsider.parser.f345.OwnershipDoc;
import com.followinsider.parser.ref.FormRef;
import com.followinsider.parser.ref.FormType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.function.Supplier;

@Service
@Slf4j
public class InsiderFormService {

    private final FormRefLoader formRefLoader;

    private final OwnershipDocLoader ownershipDocLoader;

    private final InsiderFormRepository insiderFormRepository;

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final EntitySaverService entitySaverService;

    public InsiderFormService(EdgarClient edgarClient,
                              InsiderFormRepository insiderFormRepository,
                              FiscalQuarterRepository fiscalQuarterRepository,
                              EntitySaverService entitySaverService) {

        this.formRefLoader = new FormRefLoader(edgarClient, FormType.F4);
        this.ownershipDocLoader = new OwnershipDocLoader(edgarClient);
        this.insiderFormRepository = insiderFormRepository;
        this.fiscalQuarterRepository = fiscalQuarterRepository;
        this.entitySaverService = entitySaverService;
    }

    @Async
    public void saveByQuarter(int year, int quarter) throws ParseException, IOException {
        FiscalQuarter fiscalQuarter = fiscalQuarterRepository.findByYearValAndQuarterVal(year, quarter)
                .orElseThrow(() -> new IllegalArgumentException("Invalid year and/or quarter"));

        saveByQuarter(fiscalQuarter);
    }

    @Async
    public void saveByQuarter(FiscalQuarter fiscalQuarter) throws ParseException, IOException {
        String source = fiscalQuarter.getAlias();
        logFormRequest(source);

        if (fiscalQuarter.isFullyLoaded()) {
            log.info("Rejected form request :: source: {} error: {}",
                    source, "fiscal quarter is already fully loaded");
            return;
        }

        int year = fiscalQuarter.getYearVal();
        int quarter = fiscalQuarter.getQuarterVal();

        List<FormRef> refs = formRefLoader.loadByQuarter(year, quarter);
        fiscalQuarter.setFormNum(refs.size());

        try {
            saveByRefs(refs, source);
            fiscalQuarter.setFullyLoaded(true);
        } finally {
            fiscalQuarterRepository.save(fiscalQuarter);
        }
    }

    @Async
    public void saveDaysAgo(int daysAgo) throws IOException, ParseException {
        String scope = daysAgo + " days ago";
        logFormRequest(scope);
        List<FormRef> refs = formRefLoader.loadDaysAgo(daysAgo);
        saveByRefs(refs, scope);
    }

    @Async
    public void saveLatest(int count) throws ParseException, IOException {
        String scope = "latest " + count;
        logFormRequest(scope);
        List<FormRef> refs = formRefLoader.loadLatest(0, count);
        saveByRefs(refs, scope);
    }

    @Async
    public void saveByCik(String cik) throws IOException, ParseException {
        String scope = "CIK " + cik;
        logFormRequest(scope);
        List<FormRef> refs = formRefLoader.loadByCik(cik);
        saveByRefs(refs, scope);
    }

    private void logFormRequest(String source) {
        log.info("Received form request :: source: {}", source);
    }

    private void saveByRefs(List<FormRef> refs, String source) throws ParseException, IOException {
        if (refs == null || refs.isEmpty()) return;

        log.info("Loading forms by refs :: count: {}, source: {}", refs.size(), source);

        List<InsiderForm> forms = loadByRefs(filterOldRefs(refs));
        entitySaverService.saveInsiderForms(forms, source);
    }

    private List<InsiderForm> loadByRefs(List<FormRef> refs) throws ParseException, IOException {
        List<InsiderForm> forms = new ArrayList<>();

        // ----------------------------------------------------
        System.out.println("------------------------");
        int i = 0;
        long test = 0;
        // ----------------------------------------------------

        for (FormRef ref : refs) {

            // ----------------------------------------------------
            long test1 = System.currentTimeMillis();
            OwnershipDoc doc = ownershipDocLoader.loadByRef(ref);
            test += System.currentTimeMillis() - test1;
            // ----------------------------------------------------

            InsiderForm form = InsiderFormParser.parseOwnershipDoc(doc);
            forms.add(form);

            // ----------------------------------------------------
            i++;
            if (i % 10 == 0) {
                System.out.println(test + " ms");
                test = 0;
            }
            // ----------------------------------------------------
        }
        return forms;
    }

    private List<FormRef> filterOldRefs(List<FormRef> refs) {
        List<Date> dates = refs.stream().map(FormRef::getFiledAt).toList();

        Date date1 = Collections.min(dates);
        Date date2 = Collections.max(dates);

        Set<String> ids = insiderFormRepository.findIdsFiledBetween(date1, date2);

        return refs.stream()
                .filter(ref -> !ids.contains(ref.getAccNum()))
                .toList();
    }

}
