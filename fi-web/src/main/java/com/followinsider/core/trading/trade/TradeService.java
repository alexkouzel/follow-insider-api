package com.followinsider.core.trading.trade;

import com.followinsider.core.trading.trade.Trade;
import com.followinsider.core.trading.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {

    private final TradeRepository repository;

    public List<Trade> getAll() {
        return repository.findAll();
    }

    public Trade getById(int id) {
        return repository.findById(id).orElse(null);
    }

}
