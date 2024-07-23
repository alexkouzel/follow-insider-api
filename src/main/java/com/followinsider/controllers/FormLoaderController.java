package com.followinsider.controllers;

import com.followinsider.modules.trading.form.loader.FormLoader;
import com.followinsider.modules.trading.form.loader.FormLoaderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/formloader")
@RequiredArgsConstructor
public class FormLoaderController {

    private final FormLoader formLoader;

    @GetMapping("/progress")
    public void progress() {
        // TODO: Implement this.
    }

    @PostMapping("/latest")
    public void latest() {
        formLoader.loadLatest();
    }

    @PostMapping("/last/days/{days}")
    public void lastDays(@PathVariable int days) {
        formLoader.loadLastDays(days);
    }

    @PostMapping("/cik/{cik}")
    public void cik(@PathVariable String cik) {
        formLoader.loadByCik(cik);
    }

    @PostMapping("/year/{year}/quarter/{quarter}")
    public void fiscalQuarter(@PathVariable int year, @PathVariable int quarter) {
        formLoader.loadFiscalQuarter(year, quarter);
    }

    @PostMapping("/from/{from}/to/{to}")
    public void range(@PathVariable String from, @PathVariable String to) {
        formLoader.loadFiscalQuarterRange(from, to);
    }

    @PostMapping("/status/{status}")
    public void status(@PathVariable FormLoaderStatus status) {
        formLoader.loadByLoaderStatus(status);
    }

}
