package com.followinsider.core.trading.quarter.sync;

public record QuarterSyncProgress(

        int total,

        int pending,

        int failed,

        int partial,

        int full,

        int verified

) {}
