package com.followinsider.modules.trading.form.loader;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FormLoaderScheduledService {

    private final ScopedFormLoader scopedFormLoader;

    @Value("${edgar.form_auto_load}")
    private boolean formAutoLoad;

    @Scheduled(cron = "0 */5 * * * *") // every 5 minutes
    public void loadLatestForms() {
        if (!formAutoLoad) return;
        scopedFormLoader.loadLatest();
    }

    @Scheduled(cron = "0 0 2 * * *") // every day at 2:00
    public void loadTodayForms() {
        if (!formAutoLoad) return;
        scopedFormLoader.loadLastDays(1);
    }

    // TODO: Implement loadLastFiscalQuarterForms().

}
