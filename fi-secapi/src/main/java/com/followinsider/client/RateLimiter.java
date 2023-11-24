package com.followinsider.client;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {

    private final Semaphore semaphore;

    private final ScheduledExecutorService scheduler;

    private final AtomicInteger backoffCounter = new AtomicInteger(0);

    private final int requestsPerSecond;

    private final static int INITIAL_RETRY_DELAY_SECONDS = 1;

    private final static int MAX_BACKOFF_COUNT = 5;

    public RateLimiter(int requestsPerSecond) {
        this.requestsPerSecond = requestsPerSecond;
        semaphore = new Semaphore(requestsPerSecond);
        scheduler = getDaemonScheduler();
        scheduler.scheduleAtFixedRate(this::refillPermits, 1, 1, TimeUnit.SECONDS);
    }

    private void refillPermits() {
        semaphore.release(requestsPerSecond - semaphore.availablePermits());
    }

    public void acquirePermit() throws InterruptedException {
        semaphore.acquire();
    }

    public void handleTooManyRequests() {
        int backoffCount = backoffCounter.incrementAndGet();
        int mult = (int) Math.pow(2, Math.min(backoffCount, MAX_BACKOFF_COUNT));
        int delaySeconds = INITIAL_RETRY_DELAY_SECONDS * mult;
        try {
            TimeUnit.SECONDS.sleep(delaySeconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void resetBackoff() {
        backoffCounter.set(0);
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }

    private ScheduledExecutorService getDaemonScheduler() {
        return Executors.newScheduledThreadPool(1, runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true);
            return thread;
        });
    }

}

