package com.followinsider.core.service;

import com.followinsider.core.entity.*;
import com.followinsider.core.repository.CompanyRepository;
import com.followinsider.core.repository.InsiderFormRepository;
import com.followinsider.core.repository.InsiderRepository;
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
public class EntitySaverService {

    private final CompanyRepository companyRepository;

    private final InsiderRepository insiderRepository;

    private final InsiderFormRepository insiderFormRepository;

    @Transactional
    public void saveInsiderForms(List<InsiderForm> forms, String source) {
        List<Company> companies = new ArrayList<>();
        List<Insider> insiders = new ArrayList<>();
        int tradeNum = 0;

        for (InsiderForm form : forms) {
            companies.add(form.getCompany());
            insiders.add(form.getInsider());
            tradeNum += form.getTrades().size();
        }

        int companyNum = saveCompanies(companies);
        int insiderNum = saveInsiders(insiders);

        for (InsiderForm form : forms) {
            form.setInsider(insiderRepository.getReferenceById(form.getInsider().getCik()));
            form.setCompany(companyRepository.getReferenceById(form.getCompany().getCik()));

            for (Trade trade : form.getTrades()) {
                trade.setForm(form);
            }
        }

        insiderFormRepository.saveAll(forms);
        log.info("Saved entities :: forms: {}, trades: {}, companies: {}, insiders: {}, source: {}",
                forms.size(), tradeNum, companyNum, insiderNum, source);
    }

    @Transactional
    public int saveCompanies(List<Company> companies) {
        Set<String> ids = getEntityIds(companies);
        Set<String> savedIds = companyRepository.findIdsByIdIn(ids);

        List<Company> newCompanies = companies.stream()
                .filter(company -> !savedIds.contains(company.getCik()))
                .toList();

        companyRepository.saveAll(newCompanies);
        return newCompanies.size();
    }

    @Transactional
    public int saveInsiders(List<Insider> insiders) {
        Set<String> ids = getEntityIds(insiders);
        Set<String> savedIds = insiderRepository.findIdsByIdIn(ids);

        List<Insider> newInsiders = insiders.stream()
                .filter(insider -> !savedIds.contains(insider.getCik()))
                .toList();

        insiderRepository.saveAll(newInsiders);
        return newInsiders.size();
    }

    private <T> Set<T> getEntityIds(List<? extends Identifiable<T>> entities) {
        return entities.stream().map(Identifiable::getId).collect(Collectors.toSet());
    }

}
