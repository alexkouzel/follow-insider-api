package com.followinsider.common.utils;

import com.followinsider.common.entities.sync.SyncProgress;
import com.followinsider.common.entities.sync.SyncStatus;
import com.followinsider.common.entities.sync.Synchronizable;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SyncUtils {

    /** Gets the current synchronization progress for a list of entities. */
    public <T extends Synchronizable> SyncProgress getProgress(List<T> list) {
        return new SyncProgress(
                list.size(),
                count(list, SyncStatus.PENDING),
                count(list, SyncStatus.FAILED),
                count(list, SyncStatus.PARTIAL),
                count(list, SyncStatus.FULL),
                count(list, SyncStatus.VERIFIED)
        );
    }

    /** Counts the number of entities with a specific synchronization status. */
    private <T extends Synchronizable> int count(List<T> list, SyncStatus status) {
        return (int) list.stream()
                .filter(e -> e.getSyncStatus() == status)
                .count();
    }

}
