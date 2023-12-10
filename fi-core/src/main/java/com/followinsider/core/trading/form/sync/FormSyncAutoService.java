package com.followinsider.core.trading.form.sync;

import com.followinsider.common.entities.ExecMode;
import com.followinsider.core.trading.quarter.FiscalQuarter;
import com.followinsider.core.trading.quarter.FiscalQuarterService;
import com.followinsider.core.trading.quarter.FiscalQuarterUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormSyncAutoService {

    private final FormSyncService formSyncService;

    private final FiscalQuarterService fiscalQuarterService;

    @Value("${app.auto_form_sync}")
    private boolean autoFormSync;

    @Bean
    public void loadAllForms() {
        if (!autoFormSync) return;

        List<FiscalQuarter> fiscalQuarters = fiscalQuarterService.getUnloaded();
        FiscalQuarterUtils.sortDesc(fiscalQuarters);

        for (FiscalQuarter fiscalQuarter : fiscalQuarters) {
            formSyncService.syncFiscalQuarter(fiscalQuarter, ExecMode.SYNC);
        }
    }

}
