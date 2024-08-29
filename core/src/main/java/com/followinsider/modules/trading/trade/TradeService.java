package com.followinsider.modules.trading.trade;

import com.followinsider.common.models.dtos.PageRequestDto;
import com.followinsider.modules.trading.trade.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    @Cacheable("trade")
    public List<TradeDto> getPage(TradePageRequestDto tradePageRequest) {
        System.out.println("GET PAGE");
        Pageable pageable = getPageRequest(tradePageRequest.pageRequest());
        return getPage(pageable, tradePageRequest.tradeFilters());
    }

    private List<TradeDto> getPage(Pageable pageable, TradeFiltersDto filters) {
        Specification<Trade> spec = new TradeSpecification(filters);

        return tradeRepository
                .findAll(spec, pageable)
                .map(trade -> TradeUtils.toDto(trade, true))
                .getContent();
    }

    private PageRequest getPageRequest(PageRequestDto pageRequest) {
        return PageRequest.of(
                pageRequest.pageIdx(),
                pageRequest.pageSize(),
                Sort.by("executedAt").descending()
        );
    }

    @Cacheable("trade_count")
    public long count(TradeFiltersDto filters) {
        System.out.println("COUNT");
        Specification<Trade> spec = new TradeSpecification(filters);
        return tradeRepository.count(spec);
    }

}
