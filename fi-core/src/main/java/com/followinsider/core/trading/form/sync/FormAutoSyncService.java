package com.followinsider.core.trading.form.sync;

import com.followinsider.common.utils.ListUtils;
import com.followinsider.core.trading.quarter.Quarter;
import com.followinsider.core.trading.quarter.QuarterService;
import com.followinsider.core.trading.quarter.QuarterUtils;
import com.followinsider.data.forms.refs.FormRef;
import com.followinsider.data.forms.refs.FormRefLoader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormAutoSyncService {

    private final QuarterService quarterService;

    private final FormSyncService formSyncService;

    private final FormRefLoader formRefLoader;

    private Set<String> prevIds = new HashSet<>();

    @Value("${trading.form_auto_sync}")
    private boolean formAutoSync;

    @PostConstruct
    public void init() {
        if (!formAutoSync) return;

        // TODO: Test all scheduled tasks.

//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.scheduleWithFixedDelay(this::syncLatest, 0, 2, TimeUnit.MINUTES);
//        scheduler.scheduleAtFixedRate(this::syncLastDay, 0, 1, TimeUnit.DAYS);
    }

    private void syncLatest() {
        List<FormRef> refs = formRefLoader.loadLatest(0, 100);
        List<FormRef> newRefs = ListUtils.filter(refs, ref -> !prevIds.contains(ref.accNum()));

        if (!newRefs.isEmpty()) {
            formSyncService.safeSyncRefs(newRefs, "(auto) latest");
            prevIds = refs.stream().map(FormRef::accNum).collect(Collectors.toSet());
        }
    }

    private void syncLastDay() {
        formSyncService.syncDaysAgo(1);
    }

}
