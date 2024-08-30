package com.followinsider.modules.trading.company;

import com.followinsider.common.models.requests.GetPageRequest;
import com.followinsider.common.models.requests.SearchRequest;
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

    @PostConstruct
    public void init() {
        if (companyRepository.count() == 0) {
            int count = companyLoader.loadAll();
            log.info("Loaded companies :: count: {}", count);
        }
    }

    public List<CompanyView> getPage(GetPageRequest getPageRequest) {
        return getPage(getPageRequest.pageIdx(), getPageRequest.pageSize());
    }

    public List<CompanyView> getPage(int pageIdx, int pageSize) {
        Pageable pageable = PageRequest.of(pageIdx, pageSize);
        return companyRepository.findPage(pageable).getContent();
    }

    public List<CompanyView> search(SearchRequest searchRequest) {
        return search(searchRequest.text(), searchRequest.limit());
    }

    private List<CompanyView> search(String text, int limit) {

        // Handle full names -> NAME (TICKER)
        int parIdx = text.indexOf("(");
        if (parIdx != -1)
            text = text.substring(0, parIdx);

        text = text.trim().toUpperCase();
        Pageable pageable = PageRequest.of(0, limit);
        return companyRepository.findLike(text, pageable);
    }

    public CompanyView getByCik(int cik) {
        return companyRepository.findViewById(cik);
    }

    public Set<Integer> getCiksPresentIn(Set<Integer> ids) {
        return companyRepository.findIdsPresentIn(ids);
    }

    public Company getReferenceByCik(int cik) {
        return companyRepository.getReferenceById(cik);
    }

    public void saveAll(List<Company> companies) {
        companyRepository.saveAll(companies);
    }

}
