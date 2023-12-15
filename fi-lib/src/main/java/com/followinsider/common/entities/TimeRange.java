package com.followinsider.common.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public record TimeRange(LocalDate from, LocalDate to) {

    public TimeRange(Set<LocalDate> dates) {
        this(Collections.min(dates), Collections.max(dates));
    }

    public long minutes() {
        return duration(ChronoUnit.MINUTES);
    }

    public long hours() {
        return duration(ChronoUnit.HOURS);
    }

    public long days() {
        return duration(ChronoUnit.DAYS);
    }

    public long duration(ChronoUnit unit) {
        return unit.between(from, to);
    }

}
