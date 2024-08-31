package com.followinsider.modules.trading.trade;

import com.followinsider.modules.trading.trade.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    @Cacheable("trade")
    public List<TradeView> getPage(TradePageRequest request) {
        Specification<Trade> spec = new TradeSpecification(request.tradeFilters());

        Sort sort = Sort.by(Sort.Direction.DESC, "executedAt");
        Pageable pageable = request.getPageRequest().prepare(sort);

        List<Integer> ids = tradeRepository.findIdsByPage(pageable, spec);
        return tradeRepository.findByIds(ids);
    }

    @Cacheable("trade_count")
    public long count(TradeFilters filters) {
        Specification<Trade> spec = new TradeSpecification(filters);
        return tradeRepository.count(spec);
    }

}
