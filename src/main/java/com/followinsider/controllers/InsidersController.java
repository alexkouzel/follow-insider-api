package com.followinsider.controllers;

import com.followinsider.modules.trading.form.FormService;
import com.followinsider.modules.trading.form.models.FormView;
import com.followinsider.modules.trading.insider.InsiderService;
import com.followinsider.modules.trading.insider.models.InsiderView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insiders")
@RequiredArgsConstructor
public class InsidersController {

    private final InsiderService insiderService;

    private final FormService formService;

    @GetMapping
    public List<InsiderView> page(@RequestParam(defaultValue = "0") int page) {
        return insiderService.getPage(page);
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
