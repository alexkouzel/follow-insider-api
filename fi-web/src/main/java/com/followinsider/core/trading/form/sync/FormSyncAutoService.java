package com.followinsider.core.trading.form.sync;

import com.followinsider.common.entity.ExecMode;
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

    @Scheduled(fixedDelay = 10)
    public void loadLatestUnloaded() {
        if (!autoFormSync) return;

        FiscalQuarter fiscalQuarter = fiscalQuarterService.getLatestUnloaded();
        formSyncService.syncFiscalQuarter(fiscalQuarter, ExecMode.SYNC);
    }

}
