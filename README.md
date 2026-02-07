# Time-Aware-Traffic-Routing-Engine

A production-style traffic routing service inspired by Google Maps, built using
Spring Boot, PostgreSQL, Redis, and the A* pathfinding algorithm.

## Key Features
- Time-aware routing with dynamic traffic simulation
- A* algorithm for optimal path computation
- Redis caching with TTL and graph versioning
- PostgreSQL-backed static road graph
- Scheduler-based traffic updates
- Metrics for latency and cache efficiency

## Tech Stack
- Java 17
- Spring Boot
- PostgreSQL
- Redis
- Docker
- A* Algorithm

## Architecture Overview
- PostgreSQL stores static road graph (nodes, edges)
- In-memory graph handles dynamic traffic
- Redis caches computed routes
- Scheduler updates traffic periodically
- Metrics track system performance

