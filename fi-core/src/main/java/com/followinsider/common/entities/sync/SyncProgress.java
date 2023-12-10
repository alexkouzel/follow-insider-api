package com.followinsider.common.entities.sync;

import lombok.Builder;

@Builder
public record SyncProgress(int total, int pending, int failed, int partial, int full, int verified) {
}
