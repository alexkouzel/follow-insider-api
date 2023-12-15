package com.followinsider.common;

public enum SyncStatus {

    PENDING,

    FAILED,

    PARTIAL,

    FULL,

    VERIFIED;

    public boolean isFull() {
        return this == FULL || this == VERIFIED;
    }

}
