package com.followinsider.controllers;

import com.followinsider.common.entities.ExecMode;
import com.followinsider.core.trading.form.sync.FormSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class FormsController {

    private final FormSyncService formSyncService;

    @PostMapping("/sync/days-ago/{daysAgo}")
    public void syncDaysAgo(@PathVariable int daysAgo) {
        formSyncService.syncDaysAgo(daysAgo, ExecMode.ASYNC);
    }

    @PostMapping("/sync/year/{year}/quarter/{quarter}")
    public void syncFiscalQuarter(@PathVariable int year, @PathVariable int quarter) {
        formSyncService.syncFiscalQuarter(year, quarter, ExecMode.ASYNC);
    }

    @PostMapping("/sync/latest/{count}")
    public void syncLatest(@PathVariable int count)  {
        formSyncService.syncLatest(count, ExecMode.ASYNC);
    }

    @PostMapping("/sync/companies/{cik}")
    public void syncCik(@PathVariable String cik) {
        formSyncService.syncCik(cik, ExecMode.ASYNC);
    }

}
