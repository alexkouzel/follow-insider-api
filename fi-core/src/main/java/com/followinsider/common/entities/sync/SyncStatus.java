package com.followinsider.common.entities.sync;

public enum SyncStatus {

    /** About to be synchronized. */
    PENDING,

    /** Failed to synchronize. */
    FAILED,

    /** Only part of the data is synchronized. */
    PARTIAL,

    /** Fully synchronized */
    FULL,

    /** Fully synchronized and verified */
    VERIFIED;

}
