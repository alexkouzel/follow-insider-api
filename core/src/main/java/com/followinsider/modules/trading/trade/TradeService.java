package com.followinsider.modules.trading.trade;

import com.followinsider.common.models.requests.GetPageRequest;
import com.followinsider.modules.trading.trade.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    @Cacheable("trade")
    public List<TradeView> getPage(TradePageRequest tradePageRequest) {
        GetPageRequest page = tradePageRequest.getPageRequest();
        TradeFilters filters = tradePageRequest.tradeFilters();

        return tradeRepository.findPage(
            page.pageSize(),
            page.offset(),
            filters.companyCik(),
            filters.executedAt(),
            filters.filedAt(),
            filters.type()
        );
    }

    @Cacheable("trade_count")
    public long count(TradeFilters filters) {
        return tradeRepository.count(
            filters.companyCik(),
            filters.executedAt(),
            filters.filedAt(),
            filters.type()
        );
    }

}
