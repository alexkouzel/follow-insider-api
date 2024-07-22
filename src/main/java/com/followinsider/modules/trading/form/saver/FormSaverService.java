package com.followinsider.modules.trading.form.saver;

import com.followinsider.common.entities.Identifiable;
import com.followinsider.modules.trading.form.models.Form;
import com.followinsider.modules.trading.company.models.Company;
import com.followinsider.modules.trading.company.CompanyRepository;
import com.followinsider.modules.trading.insider.models.Insider;
import com.followinsider.modules.trading.insider.InsiderRepository;
import com.followinsider.modules.trading.form.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormSaverService implements FormSaver {

    private final CompanyRepository companyRepository;

    private final InsiderRepository insiderRepository;

    private final FormRepository formRepository;

    @Override
    @Transactional
    public void saveForms(List<Form> forms) {
        List<Company> companies = new ArrayList<>();
        List<Insider> insiders = new ArrayList<>();

        for (Form form : forms) {
            companies.add(form.getCompany());
            insiders.add(form.getInsider());
        }

        saveCompanies(companies);
        saveInsiders(insiders);

        updateRefs(forms);
        formRepository.saveAll(forms);
    }

    private void updateRefs(List<Form> forms) {
        for (Form form : forms) {
            form.setCompany(companyRepository.getReferenceById(form.getCompany().getCik()));
            form.setInsider(insiderRepository.getReferenceById(form.getInsider().getCik()));
            form.getTrades().forEach(trade -> trade.setForm(form));
        }
    }

    private void saveCompanies(List<Company> companies) {
        Set<String> ids = getEntityIds(companies);
        Set<String> savedIds = companyRepository.findIdsPresentIn(ids);

        List<Company> newCompanies = companies.stream()
                .filter(company -> !savedIds.contains(company.getCik()))
                .toList();

        companyRepository.saveAll(newCompanies);
    }

    private void saveInsiders(List<Insider> insiders) {
        Set<String> ids = getEntityIds(insiders);
        Set<String> savedIds = insiderRepository.findIdsPresentIn(ids);

        List<Insider> newInsiders = insiders.stream()
                .filter(insider -> !savedIds.contains(insider.getCik()))
                .toList();

        insiderRepository.saveAll(newInsiders);
    }

    private <T> Set<T> getEntityIds(List<? extends Identifiable<T>> entities) {
        return entities.stream()
                .map(Identifiable::getId)
                .collect(Collectors.toSet());
    }

}
