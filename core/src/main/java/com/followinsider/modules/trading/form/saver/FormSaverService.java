package com.followinsider.modules.trading.form.saver;

import com.followinsider.common.models.Identifiable;
import com.followinsider.modules.trading.company.CompanyService;
import com.followinsider.modules.trading.form.models.Form;
import com.followinsider.modules.trading.company.models.Company;
import com.followinsider.modules.trading.insider.InsiderService;
import com.followinsider.modules.trading.insider.models.Insider;
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

    private final CompanyService companyService;

    private final InsiderService insiderService;

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
            form.setCompany(companyService.getReferenceByCik(form.getCompany().getCik()));
            form.setInsider(insiderService.getReferenceByCik(form.getInsider().getCik()));
            form.getTrades().forEach(trade -> trade.setForm(form));
        }
    }

    private void saveCompanies(List<Company> companies) {
        Set<Integer> ciks = getEntityIds(companies);
        Set<Integer> savedCiks = companyService.getCiksPresentIn(ciks);

        List<Company> newCompanies = companies.stream()
            .filter(company -> !savedCiks.contains(company.getCik()))
            .toList();

        companyService.saveAll(newCompanies);
    }

    private void saveInsiders(List<Insider> insiders) {
        Set<Integer> ciks = getEntityIds(insiders);
        Set<Integer> savedCiks = insiderService.getCiksPresentIn(ciks);

        List<Insider> newInsiders = insiders.stream()
            .filter(insider -> !savedCiks.contains(insider.getCik()))
            .toList();

        insiderService.saveAll(newInsiders);
    }

    private <T> Set<T> getEntityIds(List<? extends Identifiable<T>> entities) {
        return entities.stream()
            .map(Identifiable::getId)
            .collect(Collectors.toSet());
    }

}
