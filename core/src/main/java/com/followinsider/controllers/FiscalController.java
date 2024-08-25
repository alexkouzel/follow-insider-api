package com.followinsider.controllers;

import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterView;
import com.followinsider.modules.trading.fiscalquarter.FiscalQuarterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fiscal")
@RequiredArgsConstructor
public class FiscalController {

    private final FiscalQuarterService fiscalQuarterService;

    @GetMapping("/quarters")
    public List<FiscalQuarterView> all() {
        return fiscalQuarterService.getAll();
    }

    @GetMapping("/quarters/first")
    public FiscalQuarterView first() {
        return fiscalQuarterService.getFirst();
    }

    @GetMapping("/quarters/last")
    public FiscalQuarterView last() {
        return fiscalQuarterService.getLast();
    }

    @GetMapping("/quarters/count")
    public long count() {
        return fiscalQuarterService.count();
    }

    @GetMapping("/years/count")
    public long countYears() {
        return fiscalQuarterService.countYears();
    }

}
