package com.followinsider.core.trading.quarter;

import com.followinsider.core.trading.form.sync.SyncStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FiscalQuarterService {

    private final FiscalQuarterRepository fiscalQuarterRepository;

    public FiscalQuarter getByYearAndQuarter(int year, int quarter) {
        return fiscalQuarterRepository
                .findByYearValAndQuarterVal(year, quarter)
                .orElseThrow(() -> new IllegalArgumentException("Invalid year and/or quarter"));
    }

    public FiscalQuarter getLatestUnloaded() {
        List<FiscalQuarter> quarters = fiscalQuarterRepository.findBySyncStatus(SyncStatus.PENDING);
        FiscalQuarterUtils.sortDesc(quarters);
        return quarters.get(0);
    }

    public void save(FiscalQuarter fiscalQuarter) {
        fiscalQuarterRepository.save(fiscalQuarter);
    }

}
