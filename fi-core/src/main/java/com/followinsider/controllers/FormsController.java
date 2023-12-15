package com.followinsider.controllers;

import com.followinsider.core.trading.form.dtos.FormDto;
import com.followinsider.core.trading.form.FormService;
import com.followinsider.secapi.forms.refs.FormRef;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class FormsController {

    private final FormService formService;

    @GetMapping("/first")
    public FormDto getFirst() {
        return formService.getFirst();
    }

    @GetMapping("/last")
    public FormDto getLast() {
        return formService.getLast();
    }

    @GetMapping("/count")
    public int count(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        return formService.countBetween(from, to);
    }

}
