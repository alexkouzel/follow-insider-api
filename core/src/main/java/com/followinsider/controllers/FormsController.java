package com.followinsider.controllers;

import com.followinsider.common.models.dtos.PageRequestDto;
import com.followinsider.modules.trading.form.FormService;
import com.followinsider.modules.trading.form.models.FormView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class FormsController {

    private final FormService formService;

    @PostMapping
    public List<FormView> page(@Valid @RequestBody PageRequestDto pageRequest) {
        return formService.getPage(pageRequest);
    }

    @GetMapping("/count")
    public long count() {
        return formService.count();
    }

}
