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

    private static final int PAGE_SIZE = 20;

    private static final int MAX_SEARCH_LIMIT = 10;

    public List<InsiderView> getPage(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return insiderRepository.findAllViews(pageable);
    }

    public InsiderView getByCik(String cik) {
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

    public Set<String> getCiksPresentIn(Set<String> ciks) {
        return insiderRepository.findIdsPresentIn(ciks);
    }

    public Insider getReferenceByCik(String cik) {
        return insiderRepository.getReferenceById(cik);
    }

}
