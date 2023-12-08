package com.followinsider.client.limiters;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class RateLimiter {

    private final Semaphore semaphore;

    private final ScheduledExecutorService scheduler;

    private final AtomicInteger backoffCounter = new AtomicInteger(0);

    private final int requestsPerSecond;

    public RateLimiter(int requestsPerSecond) {
        this.requestsPerSecond = requestsPerSecond;
        semaphore = new Semaphore(requestsPerSecond);
        scheduler = getDaemonScheduler();
        scheduler.scheduleAtFixedRate(this::refillPermits, 1, 1, TimeUnit.SECONDS);
    }

    public void acquirePermit() throws InterruptedException {
        semaphore.acquire();
    }

    private void refillPermits() {
        semaphore.release(requestsPerSecond - semaphore.availablePermits());
    }

    public void resetBackoff() {
        backoffCounter.set(0);
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }

    public void handleTooManyRequests() {
        // 6000 -> 12000 -> 24000 seconds
        System.out.println("[EDGAR] Too Many Requests");
        applyExpDelay(6000L, 3);
    }

    public void handleUnknownError(int statusCode) {
        // 200 -> 400 -> 800 seconds
        System.out.println("[EDGAR] Unknown error: " + statusCode);
        applyExpDelay(200L, 3);
    }

    private void applyExpDelay(long initDelay, int maxBackoff) {
        applyDelay(backoff -> {
            int degree = Math.min(backoff, maxBackoff) - 1;
            return initDelay * (long) Math.pow(2, degree);
        });
    }

    private void applyDelay(Function<Integer, Long> delayer) {
        int backoff = backoffCounter.incrementAndGet();
        long delaySeconds = delayer.apply(backoff);
        try {
            TimeUnit.SECONDS.sleep(delaySeconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private ScheduledExecutorService getDaemonScheduler() {
        return Executors.newScheduledThreadPool(1, runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true);
            return thread;
        });
    }

}

