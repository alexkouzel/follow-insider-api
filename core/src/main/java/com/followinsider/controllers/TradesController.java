package com.followinsider.controllers;

import com.followinsider.modules.trading.trade.TradeService;
import com.followinsider.modules.trading.trade.models.TradeFilters;
import com.followinsider.modules.trading.trade.models.TradePageRequest;
import com.followinsider.modules.trading.trade.models.TradeView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradesController {

    private final TradeService tradeService;

    @PostMapping
    public List<TradeView> page(@Valid @RequestBody TradePageRequest tradePageRequest) {
        return tradeService.getPage(tradePageRequest);
    }

    @PostMapping("/count")
    public long count(@Valid @RequestBody(required = false) TradeFilters filters) {
        return tradeService.count(filters);
    }

}
