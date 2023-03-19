package com.ecommarket.challenge.model;

import java.time.Duration;

public class TokenBucket {

    private final long capacity;
    private long availableTokens;
    private long lastRefillNanoTime;
    private final long nanosToGenerateToken;

    public TokenBucket(long permit, Duration period) {
        this.capacity = permit;
        this.availableTokens = capacity;
        this.lastRefillNanoTime = System.nanoTime();
        this.nanosToGenerateToken = period.toNanos();
    }

    public synchronized boolean tryConsume(int permit) {
        refill();
        if (availableTokens < permit) {
            return false;
        }
        availableTokens -= permit;
        return true;
    }

    private void refill() {
        long now = System.nanoTime();
        long nanoSinceLastRefill = now - lastRefillNanoTime;
        if (nanoSinceLastRefill <= nanosToGenerateToken) {
            return;
        }
        long tokens = nanoSinceLastRefill / nanosToGenerateToken;

        availableTokens = Math.min(capacity, availableTokens + tokens);
        lastRefillNanoTime += nanoSinceLastRefill * nanosToGenerateToken;
    }
}
