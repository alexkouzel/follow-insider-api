package com.followinsider.secapi.client;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class RateLimiter {

    private final TimeUnit timeUnit;

    private final int rate;

    private final Semaphore semaphore;

    private final ScheduledExecutorService permitRefiller;
    
    private final AtomicInteger backoffCounter = new AtomicInteger(-1);

    private final Object delayLock = new Object();

    private boolean isDelaying = false;

    public RateLimiter(TimeUnit timeUnit, int rate) {
        this.timeUnit = timeUnit;
        this.rate = rate;
        this.semaphore = new Semaphore(rate);
        this.permitRefiller = startPermitRefiller();
    }

    public void resetBackoff() {
        backoffCounter.set(-1);
    }

    public void shutdown() {
        permitRefiller.shutdownNow();
    }

    public void acquirePermit() throws InterruptedException {
        synchronized (delayLock) {
            while (isDelaying) delayLock.wait();
            semaphore.acquire();
        }
    }

    public void applyDelaySeq(TimeUnit timeUnit, int... seq) {
        applyDelay(timeUnit, backoff -> seq[backoff < seq.length ? backoff : seq.length - 1]);
    }
    
    public void applyFixedDelay(TimeUnit timeUnit, int delay) {
        applyDelay(timeUnit, backoff -> delay);
    }

    private void applyDelay(TimeUnit timeUnit, Function<Integer, Integer> delayer) {
        synchronized (delayLock) {
            if (isDelaying) return; // Don't overflow delays
            isDelaying = true;
        }
        try {
            int backoff = backoffCounter.incrementAndGet();
            int delay = delayer.apply(backoff);

            try {
                timeUnit.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } finally {
            synchronized (delayLock) {
                isDelaying = false;
                delayLock.notifyAll();
            }
        }
    }

    private ScheduledExecutorService startPermitRefiller() {
        ScheduledExecutorService permitRefiller = startDaemonScheduler();
        permitRefiller.scheduleAtFixedRate(this::refillPermits, 1, 1, timeUnit);
        return permitRefiller;
    }

    private ScheduledExecutorService startDaemonScheduler() {
        return Executors.newScheduledThreadPool(1, runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true);
            return thread;
        });
    }

    private void refillPermits() {
        int freshPermits = rate - semaphore.availablePermits();
        semaphore.release(freshPermits);
    }

}
