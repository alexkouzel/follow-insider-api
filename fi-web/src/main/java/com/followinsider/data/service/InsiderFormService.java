package com.followinsider.data.service;

import com.followinsider.client.EdgarClient;
import com.followinsider.data.entity.Company;
import com.followinsider.data.entity.Insider;
import com.followinsider.data.parser.InsiderFormParser;
import com.followinsider.data.entity.InsiderForm;
import com.followinsider.data.repository.InsiderFormRepository;
import com.followinsider.loader.OwnershipDocLoader;
import com.followinsider.parser.f345.OwnershipDoc;
import com.followinsider.parser.ref.FormRef;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsiderFormService {

    private final EdgarClient edgarClient;

    private final InsiderFormRepository insiderFormRepository;

    private final InsiderService insiderService;

    private final CompanyService companyService;

    @Transactional
    public void loadAndSaveByRefs(List<FormRef> refs) throws ParseException, IOException {
        if (refs == null) return;

        List<FormRef> newRefs = filterExistingRefs(refs);
        List<InsiderForm> forms = loadByRefs(newRefs);

        // TODO: Think of a better way to save forms/insiders/companies.

        List<Company> companies = forms.stream().map(InsiderForm::getCompany).toList();
        companyService.saveNew(companies);

        // TODO: Assign company references to insiders.

        List<Insider> insiders = forms.stream().map(InsiderForm::getInsider).toList();
        insiderService.saveNew(insiders);

        // TODO: Assign company/insider references to forms.

        insiderFormRepository.saveAll(forms);
    }

    private List<InsiderForm> loadByRefs(List<FormRef> refs) throws ParseException, IOException {
        List<InsiderForm> forms = new ArrayList<>();
        OwnershipDocLoader docLoader = new OwnershipDocLoader(edgarClient);

        for (FormRef ref : refs) {
            OwnershipDoc doc = docLoader.loadByRef(ref);
            InsiderForm form = InsiderFormParser.parseOwnershipDoc(doc);
            forms.add(form);
        }
        return forms;
    }

    private List<FormRef> filterExistingRefs(List<FormRef> refs) {
        List<Date> dates = refs.stream().map(FormRef::getFiledAt).toList();

        Date date1 = Collections.min(dates);
        Date date2 = Collections.max(dates);

        Set<String> ids = insiderFormRepository.findIdsBetween(date1, date2);

        return refs.stream()
                .filter(ref -> !ids.contains(ref.getAccNum()))
                .toList();
    }

}
