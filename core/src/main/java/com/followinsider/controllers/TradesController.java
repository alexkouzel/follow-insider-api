package com.followinsider.controllers;

import com.followinsider.modules.trading.trade.TradeService;
import com.followinsider.modules.trading.trade.models.TradeFiltersDto;
import com.followinsider.modules.trading.trade.models.TradeDto;
import com.followinsider.modules.trading.trade.models.TradePageRequestDto;
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
    public List<TradeDto> page(@Valid @RequestBody TradePageRequestDto tradePageRequest) {
        return tradeService.getPage(tradePageRequest);
    }

    @PostMapping("/count")
    public long count(@Valid @RequestBody(required = false) TradeFiltersDto filters) {
        return tradeService.count(filters);
    }

}
