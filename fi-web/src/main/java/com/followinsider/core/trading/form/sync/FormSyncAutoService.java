package com.followinsider.core.trading.form.sync;

import com.followinsider.core.trading.quarter.FiscalQuarter;
import com.followinsider.core.trading.quarter.FiscalQuarterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormSyncAutoService {

    private final FormSyncService formSyncService;

    private final FiscalQuarterService fiscalQuarterService;

    @Value("${app.auto_form_sync}")
    private boolean autoFormSync;

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 2)
    public void doEvery2Hours() {
        if (!autoFormSync) return;

        FiscalQuarter fiscalQuarter = fiscalQuarterService.getLatestUnloaded();
        formSyncService.syncFiscalQuarter(fiscalQuarter);
    }

}
