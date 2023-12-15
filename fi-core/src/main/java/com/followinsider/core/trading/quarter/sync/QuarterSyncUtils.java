package com.followinsider.core.trading.quarter.sync;

import com.followinsider.core.trading.quarter.entities.Quarter;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class QuarterSyncUtils {

    public QuarterSyncProgress getProgress(List<Quarter> quarters) {
        return new QuarterSyncProgress(
                quarters.size(),
                countByStatus(quarters, QuarterSyncStatus.PENDING),
                countByStatus(quarters, QuarterSyncStatus.FAILED),
                countByStatus(quarters, QuarterSyncStatus.PARTIAL),
                countByStatus(quarters, QuarterSyncStatus.FULL),
                countByStatus(quarters, QuarterSyncStatus.VERIFIED)
        );
    }

    private int countByStatus(List<Quarter> quarters, QuarterSyncStatus status) {
        return (int) quarters.stream()
                .filter(e -> e.getSyncStatus() == status)
                .count();
    }

}
