package com.followinsider.core.trading.form.sync;

import com.followinsider.common.entities.Identifiable;
import com.followinsider.common.utils.ListUtils;
import com.followinsider.core.trading.company.Company;
import com.followinsider.core.trading.company.CompanyRepository;
import com.followinsider.core.trading.form.Form;
import com.followinsider.core.trading.insider.Insider;
import com.followinsider.core.trading.insider.InsiderRepository;
import com.followinsider.core.trading.form.FormRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormPersistenceService {

    private final CompanyRepository companyRepository;

    private final InsiderRepository insiderRepository;

    private final FormRepository formRepository;

    @Transactional
    public void saveForms(List<Form> forms, String source) {
        List<Company> companies = new ArrayList<>();
        List<Insider> insiders = new ArrayList<>();

        for (Form form : forms) {
            companies.add(form.getCompany());
            insiders.add(form.getInsider());
        }

        int companyNum = saveCompanies(companies);
        int insiderNum = saveInsiders(insiders);

        updateRefs(forms);
        formRepository.saveAll(forms);

        log.info("Saved {} :: forms: {}, trades: {}, companies: {}, insiders: {}",
                source, forms.size(), countTrades(forms), companyNum, insiderNum);
    }

    private int countTrades(List<Form> forms) {
        return forms.stream().mapToInt(form -> form.getTrades().size()).sum();
    }

    private void updateRefs(List<Form> forms) {
        for (Form form : forms) {
            form.setCompany(companyRepository.getReferenceById(form.getCompany().getCik()));
            form.setInsider(insiderRepository.getReferenceById(form.getInsider().getCik()));
            form.getTrades().forEach(trade -> trade.setForm(form));
        }
    }

    private int saveCompanies(List<Company> companies) {
        Set<String> ids = getEntityIds(companies);
        Set<String> savedIds = companyRepository.findIdsByIdIn(ids);

        List<Company> newCompanies = companies.stream()
                .filter(company -> !savedIds.contains(company.getCik()))
                .toList();

        companyRepository.saveAll(newCompanies);
        return newCompanies.size();
    }

    private int saveInsiders(List<Insider> insiders) {
        Set<String> ids = getEntityIds(insiders);
        Set<String> savedIds = insiderRepository.findIdsByIdIn(ids);

        List<Insider> newInsiders = insiders.stream()
                .filter(insider -> !savedIds.contains(insider.getCik()))
                .toList();

        insiderRepository.saveAll(newInsiders);
        return newInsiders.size();
    }

    private <T> Set<T> getEntityIds(List<? extends Identifiable<T>> entities) {
        return entities.stream()
                .map(Identifiable::getId)
                .collect(Collectors.toSet());
    }

}
