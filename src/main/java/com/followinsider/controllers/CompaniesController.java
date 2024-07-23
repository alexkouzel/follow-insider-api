package com.followinsider.controllers;

import com.followinsider.modules.trading.company.CompanyService;
import com.followinsider.modules.trading.company.models.CompanyView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompaniesController {

    private final CompanyService companyService;

    @GetMapping
    public List<CompanyView> page(@RequestParam(defaultValue = "0") int page) {
        return companyService.getPage(page);
    }

    @GetMapping("/{cik}")
    public CompanyView cik(@PathVariable String cik) {
        return companyService.getByCik(cik);
    }

}
