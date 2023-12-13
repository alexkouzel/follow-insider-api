package com.followinsider.core.trading.quarter;

import com.followinsider.common.entities.sync.SyncProgress;
import com.followinsider.common.entities.sync.SyncStatus;
import com.followinsider.common.utils.SyncUtils;
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
        List<Quarter> quarters = QuarterUtils.generate(2005, 2023);
        quarterRepository.saveAll(quarters);
    }

    public SyncProgress getSyncProgress() {
        List<Quarter> quarters = quarterRepository.findAll();
        return SyncUtils.getProgress(quarters);
    }

    public Quarter findByOrCreate(int yearVal, int quarterVal) {
        return findBy(yearVal, quarterVal).orElseGet(() -> {
            Quarter quarter = new Quarter(yearVal, quarterVal);
            return quarterRepository.save(quarter);
        });
    }

    public Optional<Quarter> findBy(int yearVal, int quarterVal) {
        return quarterRepository.findByYearValAndQuarterVal(yearVal, quarterVal);
    }

    public List<Quarter> findBy(SyncStatus syncStatus) {
        return quarterRepository.findBySyncStatus(syncStatus);
    }

    public Quarter save(Quarter quarter) {
        return quarterRepository.save(quarter);
    }

    public Quarter save(int yearVal, int quarterVal) {
        Quarter quarter = new Quarter(yearVal, quarterVal);
        return quarterRepository.save(quarter);
    }

    public boolean exists(Quarter quarter) {
        return exists(quarter.getYearVal(), quarter.getQuarterVal());
    }

    public boolean exists(int yearVal, int quarterVal) {
        return quarterRepository.existsByYearValAndQuarterVal(yearVal, quarterVal);
    }

}
