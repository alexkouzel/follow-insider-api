package com.followinsider.core.trading.trade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {

    private final TradeRepository tradeRepository;

    public List<Trade> getAll() {
        return tradeRepository.findAll();
    }

    public Trade getById(int id) {
        return tradeRepository.findById(id).orElse(null);
    }

}
