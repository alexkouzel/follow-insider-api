package com.followinsider.controller;

import com.followinsider.core.trading.trade.Trade;
import com.followinsider.core.trading.trade.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradesController {

    private final TradeService tradeService;

    @GetMapping
    public List<Trade> getAll() {
        return tradeService.getAll();
    }

    @GetMapping("/{id}")
    public Trade getById(@PathVariable int id) {
        return tradeService.getById(id);
    }

}
