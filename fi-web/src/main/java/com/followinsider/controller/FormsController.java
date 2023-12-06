package com.followinsider.controller;

import com.followinsider.core.trading.form.sync.FormSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
@Slf4j
public class FormsController {

    private final FormSyncService formSyncService;

    @PostMapping("/sync/days-ago/{daysAgo}")
    public void syncDaysAgo(@PathVariable int daysAgo) {
        formSyncService.syncDaysAgo(daysAgo);
    }

    @PostMapping("/sync/year/{year}/quarter/{quarter}")
    public void syncFiscalQuarter(@PathVariable int year, @PathVariable int quarter) {
        formSyncService.syncFiscalQuarter(year, quarter);
    }

    @PostMapping("/sync/latest/{count}")
    public void syncLatest(@PathVariable int count)  {
        formSyncService.syncLatest(count);
    }

    @PostMapping("/sync/companies/{cik}")
    public void syncCik(@PathVariable String cik) {
        formSyncService.syncCik(cik);
    }

}
