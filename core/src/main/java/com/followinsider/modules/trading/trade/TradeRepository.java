package com.followinsider.modules.trading.trade;

import com.followinsider.modules.trading.trade.models.Trade;
import com.followinsider.modules.trading.trade.models.TradeView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {

    @Query("SELECT t FROM Trade t " +
        "LEFT JOIN FETCH t.form f " +
        "LEFT JOIN FETCH f.company c " +
        "LEFT JOIN FETCH f.insider i " +
        "LEFT JOIN FETCH f.insiderTitles it " +
        "WHERE t.id IN :ids " +
        "ORDER BY t.executedAt"
    )
    List<TradeView> findByIds(List<Integer> ids);

    long count(Specification<Trade> spec);

}
