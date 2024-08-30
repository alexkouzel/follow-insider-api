package com.followinsider.modules.trading.company.loader;

import com.alexkouzel.client.exceptions.HttpRequestException;
import com.alexkouzel.company.ListedCompany;
import com.alexkouzel.company.ListedCompanyLoader;
import com.followinsider.modules.trading.company.CompanyRepository;
import com.followinsider.modules.trading.company.models.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyLoaderService implements CompanyLoader {

    private final ListedCompanyLoader listedCompanyLoader;

    private final CompanyRepository companyRepository;

    @Override
    public int loadAll() {
        try {
            List<Company> companies = fetchAll();
            companyRepository.saveAll(companies);
            return companies.size();

        } catch (HttpRequestException e) {
            log.error("Failed company loading :: error: '{}'", e.getMessage());
            return 0;
        }
    }

    private List<Company> fetchAll() throws HttpRequestException {
        return listedCompanyLoader.loadAll().stream().map(this::convert).toList();
    }

    private Company convert(ListedCompany company) {
        return Company.builder()
            .cik(company.id())
            .name(company.name())
            .ticker(company.ticker())
            .exchange(company.exchange())
            .build();
    }

}
