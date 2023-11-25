package com.followinsider.data.service;

import com.followinsider.data.entity.Company;
import com.followinsider.data.entity.Insider;
import com.followinsider.data.entity.InsiderForm;
import com.followinsider.data.entity.Position;
import com.followinsider.data.repository.CompanyRepository;
import com.followinsider.data.repository.InsiderFormRepository;
import com.followinsider.data.repository.InsiderRepository;
import com.followinsider.data.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntityGraphService {

    private final InsiderRepository insiderRepository;

    private final CompanyRepository companyRepository;

    private final PositionRepository positionRepository;

    private final InsiderFormRepository insiderFormRepository;

    @Transactional
    public void saveInsiderForms(List<InsiderForm> forms) {
        List<Company> companies = new ArrayList<>();
        List<Insider> insiders = new ArrayList<>();
        List<Position> positions = new ArrayList<>();

        for (InsiderForm form : forms) {
            companies.add(form.getCompany());
            insiders.add(form.getInsider());
            positions.addAll(form.getInsider().getPositions());
        }

        saveCompanies(companies);
        saveInsiders(insiders);
        savePositions(positions);

        for (InsiderForm form : forms) {
            form.setInsider(insiderRepository.getReferenceById(form.getInsider().getCik()));
            form.setCompany(companyRepository.getReferenceById(form.getCompany().getCik()));
        }

        insiderFormRepository.saveAll(forms);
    }

    private void saveCompanies(List<Company> companies) {
        List<Company> newCompanies = companies.stream()
                .filter(company -> !companyRepository.existsById(company.getCik()))
                .toList();

        companyRepository.saveAll(newCompanies);
    }

    public void saveInsiders(List<Insider> insiders) {
        List<Insider> newInsiders = insiders.stream()
                .filter(insider -> !insiderRepository.existsById(insider.getCik()))
                .toList();

        insiderRepository.saveAll(newInsiders);
    }

    private void savePositions(List<Position> positions) {
        for (int i = 0; i < positions.size(); i++) {
            Position position = positions.get(i);

            Insider insiderRef = insiderRepository.getReferenceById(position.getInsider().getCik());
            Company companyRef = companyRepository.getReferenceById(position.getCompany().getCik());

            Position existingPosition = positionRepository.findByCompanyAndInsider(companyRef, insiderRef);

            if (existingPosition == null) {
                position.setInsider(insiderRef);
                position.setCompany(companyRef);
                positions.set(i, position);
            } else {
                if (!position.getTitles().equals(existingPosition.getTitles())) {
                    existingPosition.setTitles(position.getTitles());
                }
                positions.set(i, existingPosition);
            }
        }
        positionRepository.saveAll(positions);
    }

}
