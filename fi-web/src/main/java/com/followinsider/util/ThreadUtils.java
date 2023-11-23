package com.followinsider.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@UtilityClass
public class ThreadUtils {

    public static ScheduledExecutorService getDaemonScheduler(int poolSize) {
        ThreadFactory threadFactory = getDaemonThreadFactory();
        return Executors.newScheduledThreadPool(poolSize, threadFactory);
    }

    private static ThreadFactory getDaemonThreadFactory() {
        return runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true);
            return thread;
        };
    }

}
