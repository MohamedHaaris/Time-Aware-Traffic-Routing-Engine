package com.traffic.routing.metrics;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class Metrics {

    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);

    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalLatencyMs = new AtomicLong(0);

    public void recordHit() {
        cacheHits.incrementAndGet();
    }

    public void recordMiss() {
        cacheMisses.incrementAndGet();
    }

    public void recordLatency(long latencyMs) {
        totalRequests.incrementAndGet();
        totalLatencyMs.addAndGet(latencyMs);
    }

    public long getCacheHits() {
        return cacheHits.get();
    }

    public long getCacheMisses() {
        return cacheMisses.get();
    }

    public double getCacheHitRatio() {
        long hits = cacheHits.get();
        long misses = cacheMisses.get();
        return (hits + misses) == 0 ? 0.0 : (double) hits / (hits + misses);
    }

    public double getAvgLatencyMs() {
        long requests = totalRequests.get();
        return requests == 0 ? 0.0 : (double) totalLatencyMs.get() / requests;
    }
}
