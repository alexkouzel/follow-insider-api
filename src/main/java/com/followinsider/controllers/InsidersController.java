package com.followinsider.controllers;

import com.followinsider.modules.trading.insider.InsiderService;
import com.followinsider.modules.trading.insider.models.InsiderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insiders")
@RequiredArgsConstructor
public class InsidersController {

    private final InsiderService insiderService;

    @GetMapping
    public List<InsiderDto> page(@RequestParam(defaultValue = "0") int page) {
        return insiderService.getPage(page);
    }

    @GetMapping("/{cik}")
    public InsiderDto cik(@PathVariable String cik) {
        return insiderService.getByCik(cik);
    }

}
