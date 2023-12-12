package com.followinsider.common.entities.sync;

/**
 * Interface for data synchronization operations. This involves managing data across
 * different sources and states, such as loading from an API and storing in a database.
 */
public interface Synchronizable {

    /** Gets the current synchronization status. */
    SyncStatus getSyncStatus();

}
