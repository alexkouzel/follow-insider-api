package com.followinsider.modules.trading.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

}
