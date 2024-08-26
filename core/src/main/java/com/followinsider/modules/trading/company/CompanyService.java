package com.followinsider.modules.trading.company;

import com.followinsider.modules.trading.company.loader.CompanyLoader;
import com.followinsider.modules.trading.company.models.Company;
import com.followinsider.modules.trading.company.models.CompanyView;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final CompanyLoader companyLoader;

    private static final int MAX_PAGE_SIZE = 20;

    private static final int MAX_SEARCH_LIMIT = 10;

    private static final int MAX_SEARCH_TEXT_SIZE = 200;

    @PostConstruct
    public void init() {
        if (companyRepository.count() == 0) {
            int count = companyLoader.loadAll();
            log.info("Loaded companies :: count: {}", count);
        }
    }

    public List<CompanyView> getPage(int page, int pageSize) {
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize);
        return companyRepository.findPage(pageable).getContent();
    }

    public CompanyView getByCik(int cik) {
        return companyRepository.findViewById(cik);
    }

    public List<CompanyView> search(String text, int limit) {
        if (text.length() > MAX_SEARCH_TEXT_SIZE)
            return List.of();

        // Handle full names -> NAME (TICKER)
        int parIdx = text.indexOf("(");
        if (parIdx != -1)
            text = text.substring(0, parIdx);

        text = text.trim().toUpperCase();
        limit = Math.min(limit, MAX_SEARCH_LIMIT);
        Pageable pageable = PageRequest.of(0, limit);
        return companyRepository.findLike(text, pageable);
    }

    public void saveAll(List<Company> companies) {
        companyRepository.saveAll(companies);
    }

    public Set<Integer> getCiksPresentIn(Set<Integer> ids) {
        return companyRepository.findIdsPresentIn(ids);
    }

    public Company getReferenceByCik(int cik) {
        return companyRepository.getReferenceById(cik);
    }

}
