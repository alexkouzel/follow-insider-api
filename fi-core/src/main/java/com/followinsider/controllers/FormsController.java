package com.followinsider.controllers;

import com.followinsider.core.trading.form.FormService;
import com.followinsider.core.trading.form.sync.FormSyncProgress;
import com.followinsider.core.trading.form.sync.FormSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class FormsController {

    private final FormSyncService formSyncService;

    private final FormService formService;

    @GetMapping("/quarter-range")
    public String[] getQuarterRange(
            @RequestParam("year") int year,
            @RequestParam("quarter") int quarter
    ) {
        return formService.getQuarterRange(year, quarter);
    }

    @GetMapping("/sync/progress")
    public FormSyncProgress getSyncProgress() {
        return formSyncService.getProgress();
    }

    @PostMapping("/sync/failed")
    public void syncFailed() {
        formSyncService.syncFailed();
    }

    @PostMapping("/sync/status/{status}")
    public void syncStatus(@PathVariable String status) {
        formSyncService.syncByStatus(status);
    }

    @PostMapping("/sync/days-ago/{daysAgo}")
    public void syncDaysAgo(@PathVariable int daysAgo) {
        formSyncService.syncDaysAgo(daysAgo);
    }

    @PostMapping("/sync/latest/{count}")
    public void syncLatest(@PathVariable int count) {
        formSyncService.syncLatest(count);
    }

    @PostMapping("/sync/cik/{cik}")
    public void syncCik(@PathVariable String cik) {
        formSyncService.syncCik(cik);
    }

    @PostMapping("/sync/year/{year}")
    public void syncYear(@PathVariable int year) {
        formSyncService.syncYear(year);
    }

    @PostMapping("/sync/year/{year}/quarter/{quarter}")
    public void syncQuarter(@PathVariable int year, @PathVariable int quarter) {
        formSyncService.syncQuarter(year, quarter);
    }

}
