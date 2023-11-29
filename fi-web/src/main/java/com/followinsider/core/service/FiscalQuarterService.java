package com.followinsider.core.service;

import com.followinsider.core.entity.FiscalQuarter;
import com.followinsider.core.repository.FiscalQuarterRepository;
import com.followinsider.util.FiscalUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FiscalQuarterService {

    private final InsiderFormService insiderFormService;

    private final FiscalQuarterRepository fiscalQuarterRepository;

    @PostConstruct
    public void init() {
        if (fiscalQuarterRepository.count() != 0) return;

        List<FiscalQuarter> fiscalQuarters = new ArrayList<>();

        for (int[] quarter : FiscalUtil.generateQuarters(1993, 1, 2023, 4)) {
            fiscalQuarters.add(new FiscalQuarter(quarter[0], quarter[1]));
        }
        fiscalQuarterRepository.saveAll(fiscalQuarters);
    }

//    @Scheduled(cron = "0 0 */2 * * ?")
//    public void scheduleSaveQuarter() {
//        saveLatestUnloadedQuarter();
//    }
//
//    private void saveLatestUnloadedQuarter() {
//        List<FiscalQuarter> fiscalQuarters = fiscalQuarterRepository.findUnloadedAndOrder();
//        FiscalQuarter latest = fiscalQuarters.get(0);
//        try {
//            insiderFormService.saveByQuarter(latest);
//
//        } catch (ParseException | IOException e) {
//            log.error("Couldn't save the next fiscal quarter: year={}; quarter={}; error='{}'",
//                    latest.getYear(), latest.getQuarter(), e.getMessage());
//        }
//    }

}
