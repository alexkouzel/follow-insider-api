package com.followinsider.controllers;

import com.followinsider.modules.trading.form.FormService;
import com.followinsider.modules.trading.form.models.FormView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class FormsController {

    private final FormService formService;

    @GetMapping
    public List<FormView> page(@RequestParam(defaultValue = "0") int page) {
        return formService.getPage(page);
    }

    @GetMapping("/count/between")
    public int countBetween(@RequestParam LocalDate date1,
                            @RequestParam LocalDate date2) {
        return formService.countBetween(date1, date2);
    }

    @GetMapping("/count/before")
    public int countBefore(@RequestParam LocalDate date) {
        return formService.countBefore(date);
    }

    @GetMapping("/count/after")
    public int countAfter(@RequestParam LocalDate date) {
        return formService.countAfter(date);
    }

}
