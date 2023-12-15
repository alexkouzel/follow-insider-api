package com.followinsider.core.trading.quarter.sync;

import com.followinsider.common.SyncStatus;
import com.followinsider.core.trading.quarter.entities.Quarter;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class QuarterSyncUtils {

    public QuarterSyncProgress getProgress(List<Quarter> quarters) {
        return new QuarterSyncProgress(
                quarters.size(),
                countByStatus(quarters, SyncStatus.PENDING),
                countByStatus(quarters, SyncStatus.FAILED),
                countByStatus(quarters, SyncStatus.PARTIAL),
                countByStatus(quarters, SyncStatus.FULL),
                countByStatus(quarters, SyncStatus.VERIFIED)
        );
    }

    private int countByStatus(List<Quarter> quarters, SyncStatus status) {
        return (int) quarters.stream()
                .filter(e -> e.getSyncStatus() == status)
                .count();
    }

}
