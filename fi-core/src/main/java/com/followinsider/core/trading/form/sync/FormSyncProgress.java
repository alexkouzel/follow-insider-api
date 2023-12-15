package com.followinsider.core.trading.form.sync;

import com.followinsider.core.trading.quarter.sync.QuarterSyncProgress;

public record FormSyncProgress(

        int formsTotal,

        int formsFailed,

        QuarterSyncProgress quarterProgress

) {}
