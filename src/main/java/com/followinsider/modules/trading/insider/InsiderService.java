package com.followinsider.modules.trading.insider;

import com.followinsider.modules.trading.insider.models.InsiderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsiderService {

    private final InsiderRepository insiderRepository;

    private static final int DEFAULT_PAGE_SIZE = 20;

    public List<InsiderDto> getPage(int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        return insiderRepository.findAllDtos(pageable);
    }

    public InsiderDto getByCik(String cik) {
        return insiderRepository.findDtoById(cik);
    }

}
