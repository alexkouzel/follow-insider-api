package com.followinsider.modules.trading.trade;

import com.followinsider.modules.trading.trade.models.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {

    Page<Trade> findAll(Specification<Trade> spec, Pageable pageable);

    long count(Specification<Trade> spec);

}
