package com.followinsider.controller;

import com.followinsider.core.trading.company.Company;
import com.followinsider.core.trading.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompaniesController {

    private final CompanyService companyService;

    @GetMapping
    public List<Company> getAll() {
        return companyService.getAll();
    }

    @GetMapping("/{cik}")
    public Company getByCik(@PathVariable String cik) {
        return companyService.getByCik(cik);
    }

}
