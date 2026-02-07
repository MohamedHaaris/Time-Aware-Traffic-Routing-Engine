package com.traffic.routing.controller;

import com.traffic.routing.service.RoutingService;
import org.springframework.web.bind.annotation.*;
import com.traffic.routing.dto.RouteResponse;

@RestController

public class RoutingController {

	private final RoutingService routingService;

	public RoutingController(RoutingService routingService) {
		this.routingService = routingService;
	}

	@GetMapping("/route")
	public RouteResponse getRoute(@RequestParam String from, @RequestParam String to) {
		return routingService.getRoute(from, to);
	}
}
