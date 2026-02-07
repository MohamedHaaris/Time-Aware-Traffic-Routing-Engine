package com.traffic.routing;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;

@EnableScheduling
@SpringBootApplication
public class RoutingApplication {

	@PostConstruct
	public void init() {
		// This ensures the application internal logic uses UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(RoutingApplication.class, args);
	}

}
