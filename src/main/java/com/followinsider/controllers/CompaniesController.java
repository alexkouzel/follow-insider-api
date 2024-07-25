package com.followinsider.controllers;

import com.followinsider.modules.trading.company.CompanyService;
import com.followinsider.modules.trading.company.models.CompanyView;
import com.followinsider.modules.trading.form.FormService;
import com.followinsider.modules.trading.form.models.FormView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompaniesController {

    private final CompanyService companyService;

    private final FormService formService;

    @GetMapping
    public List<CompanyView> page(@RequestParam(defaultValue = "0") int page) {
        return companyService.getPage(page);
    }

    @GetMapping("/{cik}")
    public CompanyView cik(@PathVariable int cik) {
        return companyService.getByCik(cik);
    }

    @GetMapping("/{cik}/forms")
    public List<FormView> forms(@PathVariable int cik) {
        return formService.getByCompanyCik(cik);
    }

}
