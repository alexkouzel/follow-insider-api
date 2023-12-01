package com.followinsider.core.trading.trade;

import com.followinsider.core.trading.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
