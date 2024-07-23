package com.followinsider.modules.trading.company;

import com.followinsider.modules.trading.company.models.CompanyView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private static final int DEFAULT_PAGE_SIZE = 20;

    public List<CompanyView> getPage(int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        return companyRepository.findAllViews(pageable);
    }

    public CompanyView getByCik(String cik) {
        return companyRepository.findViewsById(cik);
    }

}
