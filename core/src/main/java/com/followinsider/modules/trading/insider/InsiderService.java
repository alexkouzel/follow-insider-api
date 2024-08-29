package com.followinsider.modules.trading.insider;

import com.followinsider.common.models.dtos.PageRequestDto;
import com.followinsider.common.models.dtos.SearchRequestDto;
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

    public List<InsiderView> getPage(PageRequestDto pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.pageIdx(), pageRequest.pageSize());
        return insiderRepository.findPage(pageable).getContent();
    }

    public List<InsiderView> search(SearchRequestDto searchRequest) {
        return search(searchRequest.text(), searchRequest.limit());
    }

    private List<InsiderView> search(String text, int limit) {
        text = text.trim().toUpperCase();
        Pageable pageable = PageRequest.of(0, limit);
        return insiderRepository.findLike(text, pageable);
    }

    public InsiderView getByCik(int cik) {
        return insiderRepository.findViewById(cik);
    }

    public Set<Integer> getCiksPresentIn(Set<Integer> ciks) {
        return insiderRepository.findIdsPresentIn(ciks);
    }

    public Insider getReferenceByCik(int cik) {
        return insiderRepository.getReferenceById(cik);
    }

    public void saveAll(List<Insider> insiders) {
        insiderRepository.saveAll(insiders);
    }

}
