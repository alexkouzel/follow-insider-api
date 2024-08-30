package com.followinsider.controllers;

import com.followinsider.common.models.requests.GetPageRequest;
import com.followinsider.common.models.requests.SearchRequest;
import com.followinsider.modules.trading.form.FormService;
import com.followinsider.modules.trading.form.models.FormView;
import com.followinsider.modules.trading.insider.InsiderService;
import com.followinsider.modules.trading.insider.models.InsiderView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insiders")
@RequiredArgsConstructor
public class InsidersController {

    private final InsiderService insiderService;

    private final FormService formService;

    @PostMapping
    public List<InsiderView> page(@Valid @RequestBody GetPageRequest getPageRequest) {
        return insiderService.getPage(getPageRequest);
    }

    @PostMapping("/search")
    public List<InsiderView> search(@Valid @RequestBody SearchRequest searchRequest) {
        return insiderService.search(searchRequest);
    }

    @GetMapping("/{cik}")
    public InsiderView cik(@PathVariable int cik) {
        return insiderService.getByCik(cik);
    }

    @GetMapping("/{cik}/forms")
    public List<FormView> forms(@PathVariable int cik) {
        return formService.getByInsiderCik(cik);
    }

}
