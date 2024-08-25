package com.followinsider.modules.trading.insider;

import com.followinsider.modules.trading.insider.models.Insider;
import com.followinsider.modules.trading.insider.models.InsiderView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InsiderService {

    private final InsiderRepository insiderRepository;

    private static final int MAX_PAGE_SIZE = 20;

    private static final int MAX_SEARCH_LIMIT = 10;

    public List<InsiderView> getPage(int page, int pageSize) {
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize);
        return insiderRepository.findPage(pageable).getContent();
    }

    public InsiderView getByCik(int cik) {
        return insiderRepository.findViewById(cik);
    }

    public List<InsiderView> search(String text, int limit) {
        text = text.toUpperCase();
        limit = Math.min(limit, MAX_SEARCH_LIMIT);
        Pageable pageable = PageRequest.of(0, limit);
        return insiderRepository.findLike(text, pageable);
    }

    public void saveAll(List<Insider> insiders) {
        insiderRepository.saveAll(insiders);
    }

    public Set<Integer> getCiksPresentIn(Set<Integer> ciks) {
        return insiderRepository.findIdsPresentIn(ciks);
    }

    public Insider getReferenceByCik(int cik) {
        return insiderRepository.getReferenceById(cik);
    }

}
