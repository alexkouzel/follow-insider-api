package com.followinsider.common.utils;

import com.followinsider.common.entities.sync.SyncProgress;
import com.followinsider.common.entities.sync.SyncStatus;
import com.followinsider.common.entities.sync.Synchronizable;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SyncUtils {

    public <T extends Synchronizable> SyncProgress getProgress(List<T> list) {
        int total = list.size();

        int pending = count(list, SyncStatus.PENDING);
        int failed = count(list, SyncStatus.FAILED);
        int partial = count(list, SyncStatus.PARTIAL);
        int full = count(list, SyncStatus.FULL);
        int verified = count(list, SyncStatus.VERIFIED);

        return new SyncProgress(total, pending, failed, partial, full, verified);
    }

    private <T extends Synchronizable> int count(List<T> list, SyncStatus status) {
        return (int) list.stream()
                .filter(el -> el.getSyncStatus() == status)
                .count();
    }

}
