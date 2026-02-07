package com.traffic.routing.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TrafficScheduler {

    private final RoutingService routingService;

    public TrafficScheduler(RoutingService routingService) {
        this.routingService = routingService;
    }

    @Scheduled(fixedRate = 60_000) // every 1 minute
    public void updateTraffic() {
        routingService.applyTimeBasedTraffic();
    }
}
