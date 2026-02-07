package com.traffic.routing.controller;

import com.traffic.routing.metrics.Metrics;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

	private final Metrics metrics;

	public MetricsController(Metrics metrics) {
		this.metrics = metrics;
	}

	@GetMapping
	public Map<String, Object> getMetrics() {
		return Map.of("cacheHits", metrics.getCacheHits(), "cacheMisses", metrics.getCacheMisses(), "cacheHitRatio",
				metrics.getCacheHitRatio(), "avgLatencyMs", metrics.getAvgLatencyMs());
	}
}
