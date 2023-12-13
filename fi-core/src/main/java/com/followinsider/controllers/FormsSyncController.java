package com.followinsider.controllers;

import com.followinsider.common.entities.sync.SyncStatus;
import com.followinsider.core.trading.form.sync.FormSyncProgress;
import com.followinsider.core.trading.form.sync.FormSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forms/sync")
@RequiredArgsConstructor
public class FormsSyncController {

    private final FormSyncService formSyncService;

    @GetMapping("/progress")
    public FormSyncProgress getProgress() {
        return formSyncService.getProgress();
    }

    @PostMapping("/last-days")
    public void syncLastDays() {
        formSyncService.syncLastDays();
    }

    @PostMapping("/verify-quarters")
    public void verifyQuarters() {
        formSyncService.verifyQuarters();
    }

    @PostMapping("/failed")
    public void syncFailed() {
        formSyncService.syncFailed();
    }

    @PostMapping("/status/{status}")
    public void syncStatus(@PathVariable SyncStatus status) {
        formSyncService.syncByStatus(status);
    }

    @PostMapping("/days-ago/{daysAgo}")
    public void syncDaysAgo(@PathVariable int daysAgo) {
        formSyncService.syncDaysAgo(daysAgo);
    }

    @PostMapping("/latest/{count}")
    public void syncLatest(@PathVariable int count) {
        formSyncService.syncLatest(count);
    }

    @PostMapping("/company/{cik}")
    public void syncCompany(@PathVariable String cik) {
        formSyncService.syncCompany(cik);
    }

    @PostMapping("/year/{year}/quarter/{quarter}")
    public void syncQuarter(@PathVariable int year, @PathVariable int quarter) {
        formSyncService.syncQuarter(year, quarter);
    }

}
