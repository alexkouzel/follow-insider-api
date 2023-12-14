package com.followinsider.controllers;

import com.followinsider.core.trading.form.FormService;
import com.followinsider.core.trading.form.dto.FormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class FormsController {

    private final FormService formService;

    @GetMapping("/first")
    public FormDto getFirst() {
        return formService.getFirst();
    }

    @GetMapping("/first/quarter/{alias}")
    public FormDto getFirstByQuarter(@PathVariable String alias) {
        return formService.getQuarterFirst(alias);
    }

    @GetMapping("/last")
    public FormDto getLast() {
        return formService.getLast();
    }

    @GetMapping("/count")
    public int count() {
        return formService.count();
    }

    @GetMapping("/count/before/{unixTime}")
    public int countBefore(@PathVariable long unixTime) {
        return formService.countBefore(new Date(unixTime));
    }

    @GetMapping("/count/after/{unixTime}")
    public int countAfter(@PathVariable long unixTime) {
        return formService.countAfter(new Date(unixTime));
    }

}
