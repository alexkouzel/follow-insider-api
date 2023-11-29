package com.followinsider.core.controller;

import com.followinsider.core.service.InsiderFormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/insider-forms")
@RequiredArgsConstructor
@Slf4j
public class InsiderFormsController {

    private final InsiderFormService insiderFormService;

    @PostMapping("/save/days-ago/{daysAgo}")
    public void saveDaysAgo(@PathVariable int daysAgo) throws IOException, ParseException {
        insiderFormService.saveDaysAgo(daysAgo);
    }

    @PostMapping("/save/year/{year}/quarter/{quarter}")
    public void saveByQuarter(@PathVariable int year, @PathVariable int quarter) throws ParseException, IOException {
        insiderFormService.saveByQuarter(year, quarter);
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
        log.error("Failed saving forms :: error: {}", e.getMessage());
    }

}
