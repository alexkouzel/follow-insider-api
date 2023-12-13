package com.followinsider.controllers;

import com.followinsider.core.trading.form.FormService;
import com.followinsider.core.trading.form.sync.FormSyncProgress;
import com.followinsider.core.trading.form.sync.FormSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class FormsController {

    private final FormService formService;

    @GetMapping("/quarter-range")
    public String[] getQuarterRange(
            @RequestParam("year") int year,
            @RequestParam("quarter") int quarter
    ) {
        return formService.getQuarterRange(year, quarter);
    }

}
