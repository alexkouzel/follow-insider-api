package com.followinsider.config;

import com.followinsider.core.trading.quarter.FiscalQuarter;
import com.followinsider.core.trading.quarter.FiscalQuarterRepository;
import com.followinsider.core.trading.quarter.FiscalQuarterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    private final FiscalQuarterRepository fiscalQuarterRepository;

    @Bean
    public void init() {
        initFiscalQuarters();
    }

    private void initFiscalQuarters() {
        if (fiscalQuarterRepository.count() != 0) return;

        List<FiscalQuarter> quarters = FiscalQuarterUtils
                .generate(1993, 1, 2023, 4)
                .stream()
                .map(vals -> new FiscalQuarter(vals[0], vals[1]))
                .toList();

        fiscalQuarterRepository.saveAll(quarters);
    }

}
