package com.followinsider.core.logging;

import java.util.Date;

public record Log(

        Date date,

        String thread,

        LogLevel level,

        String path,

        String message,

        String stackTrace
`
) {}
