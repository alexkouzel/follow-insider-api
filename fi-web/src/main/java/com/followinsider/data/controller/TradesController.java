package com.followinsider.data.controller;

import com.followinsider.data.entity.Trade;
import com.followinsider.data.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
@Slf4j
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
