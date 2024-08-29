package com.followinsider.controllers;

import com.followinsider.common.models.dtos.PageRequestDto;
import com.followinsider.common.models.dtos.SearchRequestDto;
import com.followinsider.modules.trading.company.CompanyService;
import com.followinsider.modules.trading.company.models.CompanyView;
import com.followinsider.modules.trading.form.FormService;
import com.followinsider.modules.trading.form.models.FormView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompaniesController {

    private final CompanyService companyService;

    private final FormService formService;

    @PostMapping
    public List<CompanyView> page(@Valid @RequestBody PageRequestDto pageRequest) {
        return companyService.getPage(pageRequest);
    }

    @PostMapping("/search")
    public List<CompanyView> search(@Valid @RequestBody SearchRequestDto searchRequest) {
        return companyService.search(searchRequest);
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
