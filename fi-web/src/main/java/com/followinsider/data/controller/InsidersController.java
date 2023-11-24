package com.followinsider.data.controller;

import com.followinsider.data.entity.Insider;
import com.followinsider.data.service.InsiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/insiders")
@RequiredArgsConstructor
@Slf4j
public class InsidersController {

    private final InsiderService insiderService;

    @GetMapping
    public List<Insider> getAll() {
        return insiderService.getAll();
    }

    @GetMapping("/{cik}")
    public Insider getByCik(@PathVariable String cik) {
        return insiderService.getByCik(cik);
    }

}
