package com.followinsider.data.service;

import com.followinsider.client.EdgarClient;
import com.followinsider.data.entity.*;
import com.followinsider.data.parser.InsiderFormParser;
import com.followinsider.data.repository.*;
import com.followinsider.loader.FormRefLoader;
import com.followinsider.loader.OwnershipDocLoader;
import com.followinsider.parser.f345.OwnershipDoc;
import com.followinsider.parser.ref.FormRef;
import com.followinsider.parser.ref.FormType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InsiderFormService {

    private final EdgarClient edgarClient;

    private final InsiderFormRepository insiderFormRepository;

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final EntityGraphService entityGraphService;

    public void saveByQuarter(int year, int qtr) throws ParseException, IOException {
        FiscalQuarter fiscalQuarter = fiscalQuarterRepository.findByYearAndQuarter(year, qtr)
                .orElseThrow(() -> new IllegalArgumentException("Invalid year and/or quarter"));

        saveByQuarter(fiscalQuarter);
    }

    public void saveByQuarter(FiscalQuarter fiscalQuarter) throws ParseException, IOException {
        if (fiscalQuarter.isFullyLoaded()) return;

        int year = fiscalQuarter.getYear();
        int qtr = fiscalQuarter.getQuarter();

        List<FormRef> refs = getRefLoader().loadByQuarter(year, qtr);
        fiscalQuarter.setFormNum(refs.size());

        try {
            saveByRefs(refs);
            fiscalQuarter.setFullyLoaded(true);
        } finally {
            fiscalQuarterRepository.save(fiscalQuarter);
        }
    }

    public void saveDaysAgo(int daysAgo) throws IOException, ParseException {
        List<FormRef> refs = getRefLoader().loadDaysAgo(daysAgo);
        saveByRefs(refs);
    }

    public void saveLatest(int count) throws ParseException, IOException {
        List<FormRef> refs = getRefLoader().loadLatest(0, count);
        saveByRefs(refs);
    }

    public void saveByCik(String cik) throws IOException, ParseException {
        List<FormRef> refs = getRefLoader().loadByCik(cik);
        saveByRefs(refs);
    }

    private FormRefLoader getRefLoader() {
        return new FormRefLoader(edgarClient, FormType.F4);
    }

    private void saveByRefs(List<FormRef> refs) throws ParseException, IOException {
        List<InsiderForm> forms = loadNewByRefs(refs);
        entityGraphService.saveInsiderForms(forms);
    }

    private List<InsiderForm> loadNewByRefs(List<FormRef> refs) throws ParseException, IOException {
        return loadByRefs(filterOldRefs(refs));
    }

    private List<InsiderForm> loadByRefs(List<FormRef> refs) throws ParseException, IOException {
        OwnershipDocLoader docLoader = new OwnershipDocLoader(edgarClient);
        List<InsiderForm> forms = new ArrayList<>();

        for (FormRef ref : refs) {
            OwnershipDoc doc = docLoader.loadByRef(ref);
            InsiderForm form = InsiderFormParser.parseOwnershipDoc(doc);
            forms.add(form);
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
