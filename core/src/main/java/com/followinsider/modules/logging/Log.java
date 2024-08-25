package com.followinsider.modules.logging;

import java.time.LocalDate;

public record Log(

        LocalDate timestamp,

        String threadName,

        LogLevel logLevel,

        String loggerName,

        String message,

        String stackTrace

) {}
