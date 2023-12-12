package com.followinsider.common.entities.sync;

public record SyncProgress(

        int total,

        int pending,

        int failed,

        int partial,

        int full,

        int verified

) {}
