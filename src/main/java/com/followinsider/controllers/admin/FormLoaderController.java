package com.followinsider.controllers.admin;

import com.followinsider.modules.trading.fiscalquarter.FiscalQuarterService;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterFormsView;
import com.followinsider.modules.trading.form.loader.FormLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formloader")
@RequiredArgsConstructor
public class FormLoaderController {

    private final FormLoader formLoader;

    private final FiscalQuarterService fiscalQuarterService;

    @GetMapping("/progress")
    public List<FiscalQuarterFormsView> progress() {
        return fiscalQuarterService.getFormLoaderProgress();
    }

    @PostMapping("/latest")
    public void latest() {
        formLoader.loadLatest();
    }

    @PostMapping("/last/days/{days}")
    public void lastDays(@PathVariable int days) {
        formLoader.loadLastDays(days);
    }

    @PostMapping("/company/{cik}")
    public void company(@PathVariable int cik) {
        formLoader.loadByCompany(cik);
    }

    @PostMapping("/year/{year}/quarter/{quarter}")
    public void fiscalQuarter(@PathVariable int year, @PathVariable int quarter) {
        formLoader.loadFiscalQuarter(year, quarter);
    }

    @PostMapping("/from/{from}/to/{to}")
    public void range(@PathVariable String from, @PathVariable String to) {
        formLoader.loadFiscalQuarterRange(from, to);
    }

}
