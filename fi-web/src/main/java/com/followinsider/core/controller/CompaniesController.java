package com.followinsider.core.controller;

import com.followinsider.core.service.CompanyService;
import com.followinsider.core.entity.Company;
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