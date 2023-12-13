package com.followinsider.common.entities.sync;

public enum SyncStatus {

    /** About to be synchronized. */
    PENDING,

    /** Failed to synchronize. */
    FAILED,

    /** Only specific parts of the data failed to synchronise. */
    PARTIAL,

    /** Fully synchronized and is ready to be verified. */
    FULL,

    /** Fully synchronized and verified. */
    VERIFIED;

    public boolean isFull() {
        return this == FULL || this == VERIFIED;
    }

}
