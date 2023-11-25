package com.followinsider.data.service;

import com.followinsider.data.entity.FiscalQuarter;
import com.followinsider.data.repository.FiscalQuarterRepository;
import com.followinsider.util.FiscalUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FiscalQuarterService {

    private final FiscalQuarterRepository repository;
    
    private final InsiderFormService insiderFormService;

    @PostConstruct
    public void init() {
        List<FiscalQuarter> fiscalQuarters = new ArrayList<>();

        for (int[] quarter : FiscalUtil.generateQuarters(1993, 1, 2023, 4)) {
            fiscalQuarters.add(new FiscalQuarter(quarter[0], quarter[1]));
        }
        repository.saveAll(fiscalQuarters);
    }

//    @Scheduled(cron = "0 0 */2 * * ?")
//    public void scheduleSaveQuarter() {
//        saveLatestUnloadedQuarter();
//    }

    private void saveLatestUnloadedQuarter() {
        List<FiscalQuarter> fiscalQuarters = repository.findUnloadedAndOrder();
        FiscalQuarter latest = fiscalQuarters.get(0);
        try {
            insiderFormService.saveByQuarter(latest);
        } catch (ParseException | IOException e) {
            log.error("Failed to save the next fiscal quarter: year={}; qtr={}; error='{}'",
                    latest.getYear(), latest.getQuarter(), e.getMessage());
        }
    }

}
