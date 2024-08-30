package com.followinsider.controllers;

import com.followinsider.common.models.requests.GetPageRequest;
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
    public List<FormView> page(@Valid @RequestBody GetPageRequest getPageRequest) {
        return formService.getPage(getPageRequest);
    }

    @GetMapping("/count")
    public long count() {
        return formService.count();
    }

}
