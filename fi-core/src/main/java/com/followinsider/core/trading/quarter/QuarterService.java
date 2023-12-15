package com.followinsider.core.trading.quarter;

import com.followinsider.core.trading.quarter.sync.QuarterSyncProgress;
import com.followinsider.common.SyncStatus;
import com.followinsider.core.trading.quarter.entities.Quarter;
import com.followinsider.core.trading.quarter.entities.QuarterRange;
import com.followinsider.core.trading.quarter.entities.QuarterVals;
import com.followinsider.core.trading.quarter.sync.QuarterSyncUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuarterService {

    private final QuarterRepository quarterRepository;

    @Transactional
    public void init() {
        if (quarterRepository.count() != 0) return;

        QuarterRange range = new QuarterRange(2005, 1, 2023, 4);
        List<Quarter> quarters = range.generate().stream().map(Quarter::new).toList();

        quarterRepository.saveAll(quarters);
    }

    public QuarterSyncProgress getSyncProgress() {
        List<Quarter> quarters = quarterRepository.findAll();
        return QuarterSyncUtils.getProgress(quarters);
    }

    public Optional<Quarter> findBy(QuarterVals vals) {
        return quarterRepository.findByYearAndQuarter(vals.year(), vals.quarter());
    }

    public List<Quarter> findBy(SyncStatus syncStatus) {
        return quarterRepository.findBySyncStatus(syncStatus);
    }

    public Quarter findOrSaveBy(QuarterVals vals) {
        return findBy(vals).orElseGet(() -> save(new Quarter(vals)));
    }

    public Quarter save(Quarter quarter) {
        return quarterRepository.save(quarter);
    }

}
