package com.followinsider.controllers;

import com.followinsider.core.trading.form.sync.FormSyncProgress;
import com.followinsider.core.trading.form.sync.FormSyncService;
import com.followinsider.common.SyncStatus;
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

    @PostMapping("/verify")
    public void verify() {
        formSyncService.verify();
    }

    @PostMapping("/failed")
    public void syncFailed() {
        formSyncService.syncFailed();
    }

    @PostMapping("/status/{status}")
    public void syncStatus(@PathVariable SyncStatus status) {
        formSyncService.syncByStatus(status);
    }

    @PostMapping("/last-days/{days}")
    public void syncLastDays(@PathVariable int days) {
        formSyncService.syncLastDays(days);
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

    @PostMapping("/quarter/{alias}")
    public void syncQuarter(@PathVariable String alias) {
        formSyncService.syncQuarter(alias);
    }

}
