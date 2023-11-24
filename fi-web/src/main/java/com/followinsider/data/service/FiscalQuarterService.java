package com.followinsider.data.service;

import com.followinsider.client.EdgarClient;
import com.followinsider.data.entity.FiscalQuarter;
import com.followinsider.data.repository.FiscalQuarterRepository;
import com.followinsider.loader.FormRefLoader;
import com.followinsider.parser.ref.FormRef;
import com.followinsider.parser.ref.FormType;
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

    private final EdgarClient edgarClient;

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

    /**
     * Downloads the latest fiscal quarter that is not fully loaded every 2 hours.
     */
    @Scheduled(cron = "0 0 */2 * * ?")
    public void downloadNext() {
        List<FiscalQuarter> fiscalQuarters = repository.findNotLoadedAndSort();
        if (!fiscalQuarters.isEmpty()) download(fiscalQuarters.get(0));
    }

    private void download(FiscalQuarter fiscalQuarter) {
        try {
            FormRefLoader refLoader = new FormRefLoader(edgarClient, FormType.F4);
            List<FormRef> refs = refLoader.loadByQuarter(fiscalQuarter.getYear(), fiscalQuarter.getQuarter());
            fiscalQuarter.setFormNum(refs.size());

            insiderFormService.loadAndSaveByRefs(refs);
            fiscalQuarter.setFullyLoaded(true);

        } catch (IOException | ParseException e) {
            log.error("Failed to load fiscal quarter forms: error='{}'", e.getMessage());
        } finally {
            repository.save(fiscalQuarter);
        }
    }

}
