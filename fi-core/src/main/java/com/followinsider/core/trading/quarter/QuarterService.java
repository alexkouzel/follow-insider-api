package com.followinsider.core.trading.quarter;

import com.followinsider.common.entities.sync.SyncProgress;
import com.followinsider.common.entities.sync.SyncStatus;
import com.followinsider.common.utils.SyncUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuarterService {

    private final QuarterRepository quarterRepository;

    @Transactional
    public void init() {
        if (quarterRepository.count() != 0) return;

        List<Quarter> quarters = QuarterUtils
                .generate(2005, 2023).stream()
                .map(vals -> new Quarter(vals.first(), vals.second()))
                .toList();

        quarterRepository.saveAll(quarters);
    }

    public Quarter save(Quarter quarter) {
        // TODO: Validate before saving/updating.
        return quarterRepository.save(quarter);
    }

    public SyncProgress getSyncProgress() {
        List<Quarter> quarters = quarterRepository.findAll();
        return SyncUtils.getProgress(quarters);
    }

    public List<Quarter> getBySyncStatus(SyncStatus syncStatus) {
        List<Quarter> quarters = quarterRepository.findBySyncStatus(syncStatus);
        QuarterUtils.sortDesc(quarters);
        return quarters;
    }

    public List<Quarter> getByYear(int yearVal) {
        return quarterRepository.findByYearVal(yearVal);
    }

    public Quarter getByYearAndQuarter(int year, int quarter) {
        return quarterRepository
                .findByYearValAndQuarterVal(year, quarter)
                .orElseThrow(() -> new IllegalArgumentException("Invalid year and/or quarter"));
    }

    public boolean exists(Quarter quarter) {
        return exists(quarter.getYearVal(), quarter.getQuarterVal());
    }

    public boolean exists(int year, int quarter) {
        return quarterRepository.existsByYearValAndQuarterVal(year, quarter);
    }

}
