package com.followinsider.core.controller;

import com.followinsider.core.dto.FiscalQuarterStats;
import com.followinsider.core.service.FormDownloaderService;
import com.followinsider.core.service.FormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
@Slf4j
public class FormsController {

    private final FormDownloaderService formDownloaderService;

    private final FormService formService;

    @PostMapping("/stats/year/{year}/quarter/{quarter}")
    public FiscalQuarterStats quarterStats(@PathVariable int year, @PathVariable int quarter) {
        return formService.getQuarterStats(year, quarter);
    }

    @PostMapping("/download/days-ago/{daysAgo}")
    public void downloadDaysAgo(@PathVariable int daysAgo) {
        formDownloaderService.downloadDaysAgo(daysAgo);
    }

    @PostMapping("/download/year/{year}/quarter/{quarter}")
    public void downloadQuarter(@PathVariable int year, @PathVariable int quarter) {
        formDownloaderService.downloadQuarter(year, quarter);
    }

    @PostMapping("/download/latest/{count}")
    public void downloadLatest(@PathVariable int count)  {
        formDownloaderService.downloadLatest(count);
    }

    @PostMapping("/download/companies/{cik}")
    public void downloadCik(@PathVariable String cik) {
        formDownloaderService.downloadCik(cik);
    }

}
