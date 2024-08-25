package com.followinsider.controllers;

import com.followinsider.modules.trading.trade.TradeService;
import com.followinsider.modules.trading.trade.models.TradeFiltersDto;
import com.followinsider.modules.trading.trade.models.TradeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradesController {

    private final TradeService tradeService;

    @PostMapping
    public List<TradeDto> page(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int pageSize,
                               @RequestBody(required = false) TradeFiltersDto filters) {
        return tradeService.getPage(page, pageSize, filters);
    }

    @PostMapping("/count")
    public long count(@RequestBody(required = false) TradeFiltersDto filters) {
        return tradeService.count(filters);
    }

}
