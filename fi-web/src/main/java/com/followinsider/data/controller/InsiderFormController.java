package com.followinsider.data.controller;

import com.followinsider.data.service.InsiderFormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/insider-forms")
@RequiredArgsConstructor
@Slf4j
public class InsiderFormController {

    private final InsiderFormService insiderFormService;

    @PostMapping("/save/days-ago/{daysAgo}")
    public void saveDaysAgo(@PathVariable int daysAgo) throws IOException, ParseException {
        insiderFormService.saveDaysAgo(daysAgo);
    }

    @PostMapping("/save/year/{year}/qtr/{qtr}")
    public void saveByQuarter(@PathVariable int year, @PathVariable int qtr) throws ParseException, IOException {
        insiderFormService.saveByQuarter(year, qtr);
    }

    @PostMapping("/save/latest/{count}")
    public void saveLatest(@PathVariable int count) throws ParseException, IOException {
        insiderFormService.saveLatest(count);
    }

    @PostMapping("/save/companies/{cik}")
    public void saveByCik(@PathVariable String cik) throws IOException, ParseException {
        insiderFormService.saveByCik(cik);
    }

    @ExceptionHandler({IOException.class, ParseException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleSavingException(Exception e) {
        log.error("Failed to save insider forms: error='{}'", e.getMessage());
    }

}
