package com.followinsider.modules.trading.form.loader;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FormLoaderScheduledService {

    private final FormLoader formLoader;

    private Set<String> latestAccNos = new HashSet<>();

    @Value("${sec.form_auto_load}")
    private boolean formAutoLoad;

    @Scheduled(cron = "0 */2 * * * *") // every 2 minutes
    public void loadLatestForms() {
        if (!formAutoLoad) return;
        latestAccNos = formLoader.updateLatest(latestAccNos);
    }

    @Scheduled(cron = "0 0 2 * * *") // every day at 2:00
    public void loadTodayForms() {
        if (!formAutoLoad) return;
        formLoader.loadLastDays(1);
    }

    public void loadLastFiscalQuarterForms() {
        // TODO: Implement this.
    }

}
