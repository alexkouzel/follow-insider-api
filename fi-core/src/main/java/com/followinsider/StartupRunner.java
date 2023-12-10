package com.followinsider;

import com.followinsider.core.trading.form.sync.FormSyncService;
import com.followinsider.common.entities.sync.SyncStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final FormSyncService formSyncService;

    @Value("${trading.sync_all_forms}")
    private boolean syncAllForms;

    @Override
    public void run(String... args) {
        if (syncAllForms) {
            formSyncService.syncByStatus(SyncStatus.PENDING);
        }
    }

}
