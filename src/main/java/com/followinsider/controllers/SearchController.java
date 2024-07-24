package com.followinsider.controllers;

import com.followinsider.modules.trading.company.CompanyService;
import com.followinsider.modules.trading.company.models.CompanyView;
import com.followinsider.modules.trading.insider.InsiderService;
import com.followinsider.modules.trading.insider.models.InsiderView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final CompanyService companyService;

    private final InsiderService insiderService;

    @GetMapping("/companies")
    public List<CompanyView> companies(@RequestParam String text,
                                       @RequestParam(defaultValue = "5") int limit) {
        return companyService.search(text, limit);
    }

    @GetMapping("/insiders")
    public List<InsiderView> insiders(@RequestParam String text,
                                      @RequestParam(defaultValue = "5") int limit) {
        return insiderService.search(text, limit);
    }

}
