package com.followinsider.modules.trading.company;

import com.alexkouzel.client.exceptions.HttpRequestException;
import com.alexkouzel.company.ListedCompany;
import com.alexkouzel.company.ListedCompanyLoader;
import com.followinsider.modules.trading.company.models.Company;
import com.followinsider.modules.trading.company.models.CompanyView;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final ListedCompanyLoader listedCompanyLoader;

    private static final int DEFAULT_PAGE_SIZE = 20;

    @PostConstruct
    public void init() throws HttpRequestException {
        if (companyRepository.count() != 0) return;

        List<Company> companies = listedCompanyLoader
                .loadAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());

        companyRepository.saveAll(companies);
    }

    private Company convert(ListedCompany company) {
        return Company.builder()
                .cik(company.id())
                .name(company.name())
                .ticker(company.ticker())
                .exchange(company.exchange())
                .build();
    }

    public List<CompanyView> getPage(int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        return companyRepository.findAllViews(pageable);
    }

    public CompanyView getByCik(String cik) {
        return companyRepository.findViewsById(cik);
    }

}
