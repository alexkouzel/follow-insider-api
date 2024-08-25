package com.followinsider.modules.trading.trade;

import com.followinsider.modules.trading.trade.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    private static final int MAX_PAGE_SIZE = 20;

    public List<TradeDto> getPage(int page, int pageSize, TradeFiltersDto filters) {
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("executedAt").descending());
        Specification<Trade> spec = new TradeSpecification(filters);

        return tradeRepository
                .findAll(spec, pageable)
                .map(trade -> TradeUtils.toDto(trade, true))
                .getContent();
    }

    public long count(TradeFiltersDto filters) {
        Specification<Trade> spec = new TradeSpecification(filters);
        return tradeRepository.count(spec);
    }

}
