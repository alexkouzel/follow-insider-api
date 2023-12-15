package com.followinsider.config;

import com.followinsider.core.trading.quarter.QuarterService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    private final QuarterService quarterService;

    @PostConstruct
    public void init() {
        quarterService.init();
    }

}
