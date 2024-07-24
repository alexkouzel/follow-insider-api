package com.followinsider.modules.trading.company;

import com.followinsider.modules.trading.company.loader.CompanyLoader;
import com.followinsider.modules.trading.company.models.Company;
import com.followinsider.modules.trading.company.models.CompanyView;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final CompanyLoader companyLoader;

    private static final int PAGE_SIZE = 20;

    private static final int MAX_SEARCH_LIMIT = 10;

    @PostConstruct
    public void init() {
        if (companyRepository.count() == 0) {
            companyLoader.loadAll();
        }
    }

    public List<CompanyView> getPage(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return companyRepository.findAllViews(pageable);
    }

    public CompanyView getByCik(String cik) {
        return companyRepository.findViewById(cik);
    }

    public List<CompanyView> search(String text, int limit) {
        text = text.toUpperCase();
        limit = Math.min(limit, MAX_SEARCH_LIMIT);
        Pageable pageable = PageRequest.of(0, limit);
        return companyRepository.findLike(text, pageable);
    }

    public void saveAll(List<Company> companies) {
        companyRepository.saveAll(companies);
    }

    public Set<String> getCiksPresentIn(Set<String> ids) {
        return companyRepository.findIdsPresentIn(ids);
    }

    public Company getReferenceByCik(String cik) {
        return companyRepository.getReferenceById(cik);
    }

}
