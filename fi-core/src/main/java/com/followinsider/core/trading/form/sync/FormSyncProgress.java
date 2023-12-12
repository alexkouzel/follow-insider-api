package com.followinsider.core.trading.form.sync;

import com.followinsider.common.entities.sync.SyncProgress;

public record FormSyncProgress(

        int formsTotal,

        int formsFailed,

        SyncProgress quarterProgress
) {}
