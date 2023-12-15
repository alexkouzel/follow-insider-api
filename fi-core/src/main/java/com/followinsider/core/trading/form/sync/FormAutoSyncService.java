package com.followinsider.core.trading.form.sync;

import com.followinsider.secapi.forms.refs.FormRef;
import com.followinsider.secapi.forms.refs.FormRefLoader;
import com.followinsider.secapi.forms.refs.FormRefUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FormAutoSyncService {

    private final FormSyncService formSyncService;

    private final FormRefLoader formRefLoader;

    private Set<String> latestAccNums = new HashSet<>();

    @Value("${trading.form_auto_sync}")
    private boolean formAutoSync;

    @Scheduled(cron = "0 */2 * * * *") // every 2 minutes
    public void syncLatest() {
        if (!formAutoSync) return;

        List<FormRef> refs = formRefLoader.loadLatest(0, 100);
        List<FormRef> newRefs = FormRefUtils.filterAccNums(refs, latestAccNums);
        if (newRefs.isEmpty()) return;

        formSyncService.safeSyncRefs(newRefs, "latest 100 (auto)");
        latestAccNums = FormRefUtils.getAccNums(refs);
    }

    @Scheduled(cron = "0 0 2 * * *") // every day at 2:00
    private void syncLastDay() {
        if (!formAutoSync) return;

        formSyncService.syncDaysAgo(0, "last day (auto)");
    }

}
