package com.followinsider.core.trading.trade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
