package com.followinsider.core.service;

import com.followinsider.client.EdgarClient;
import com.followinsider.core.entity.FiscalQuarter;
import com.followinsider.core.repository.FiscalQuarterRepository;
import com.followinsider.loader.FormRefLoader;
import com.followinsider.parser.ref.FormRef;
import com.followinsider.parser.ref.FormType;
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

    private final EdgarClient client;

    private final FiscalQuarterRepository repository;

    private final InsiderFormService insiderFormService;

    @PostConstruct
    public void init() {
        List<FiscalQuarter> quarters = generate(1933, 2023, 4);
        repository.saveAll(quarters);
    }

    /** Download the latest fiscal quarter that is not fully loaded every 2 hours */
    @Scheduled(cron = "0 0 */2 * * ?")
    public void downloadNext() {
        List<FiscalQuarter> quarters = repository.findNotLoadedAndSort();
        if (!quarters.isEmpty()) download(quarters.get(0));
    }

    private void download(FiscalQuarter quarter) {
        try {
            FormRefLoader refLoader = new FormRefLoader(client, FormType.F4);
            List<FormRef> refs = refLoader.loadByQuarter(quarter.getYear(), quarter.getQuarter());
            insiderFormService.loadByRefs(refs);

            quarter.setFullyLoaded(true);
            repository.save(quarter);

        } catch (IOException | ParseException e) {
            log.error("QTR download failed: error='{}'", e.getMessage());
        }
    }

    private List<FiscalQuarter> generate(int fromYear, int toYear, int toQtr) {
        List<FiscalQuarter> quarters = new ArrayList<>();

        for (int year = fromYear; year <= toYear; year++) {
            for (int qtr = 1; qtr < (year == toYear ? toQtr : 4); qtr++) {
                quarters.add(new FiscalQuarter(year, qtr));
            }
        }
        return quarters;
    }

}
