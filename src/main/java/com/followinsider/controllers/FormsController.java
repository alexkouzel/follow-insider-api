package com.followinsider.controllers;

import com.followinsider.modules.trading.form.FormService;
import com.followinsider.modules.trading.form.models.FormView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class FormsController {

    private final FormService formService;

    @GetMapping
    public List<FormView> page(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int pageSize) {
        return formService.getPage(page, pageSize);
    }

    @GetMapping("/count")
    public long count() {
        return formService.count();
    }

}
