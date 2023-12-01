package com.followinsider.controller;

import com.followinsider.core.trading.insider.Insider;
import com.followinsider.core.trading.insider.InsiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/insiders")
@RequiredArgsConstructor
public class InsidersController {

    private final InsiderService insiderService;

    @GetMapping
    public List<Insider> getAll() {
        return insiderService.getAll();
    }

    @GetMapping("/{cik}")
    public Insider getByCik(@PathVariable String cik) {
        return insiderService.getByCik(cik);
    }

}
