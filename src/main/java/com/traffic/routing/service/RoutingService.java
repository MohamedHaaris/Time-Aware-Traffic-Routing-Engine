package com.traffic.routing.service;

import com.traffic.routing.algorithm.AStar;
import com.traffic.routing.dto.RouteResponse;
import com.traffic.routing.graph.Edge;
import com.traffic.routing.graph.Graph;
import com.traffic.routing.graph.Node;
import com.traffic.routing.metrics.Metrics;
import com.traffic.routing.repository.EdgeRepository;
import com.traffic.routing.repository.NodeRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

@Service
public class RoutingService {

	/* ===================== CORE STATE ===================== */

	// In-memory graph used for fast computation
	private final Graph graph = new Graph();

	private final StringRedisTemplate redisTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Metrics metrics;

	// DB repositories (source of truth)
	private final NodeRepository nodeRepository;
	private final EdgeRepository edgeRepository;

	/* ===================== CONSTRUCTOR ===================== */

	public RoutingService(StringRedisTemplate redisTemplate, Metrics metrics, NodeRepository nodeRepository,
			EdgeRepository edgeRepository) {
		this.redisTemplate = redisTemplate;
		this.metrics = metrics;
		this.nodeRepository = nodeRepository;
		this.edgeRepository = edgeRepository;
	}

	/* ===================== DB → GRAPH LOAD ===================== */

	@PostConstruct
	public void loadGraphFromDb() {

		// Load nodes
		nodeRepository.findAll().forEach(n -> graph.addNode(new Node(n.getId(), n.getX(), n.getY())));

		// Load edges (base weights only)
		edgeRepository.findAll().forEach(e -> graph.addEdge(graph.getNodeById(e.getFromNode()),
				graph.getNodeById(e.getToNode()), e.getBaseWeight()));

		System.out.println("[DB LOAD] Graph loaded from PostgreSQL");
	}

	/* ===================== TRAFFIC UPDATE (SCHEDULER) ===================== */

	public void applyTimeBasedTraffic() {

		int newWeight;
		LocalTime now = LocalTime.now();

		if (now.isAfter(LocalTime.of(8, 0)) && now.isBefore(LocalTime.of(10, 0))) {
			newWeight = 15; // morning rush
		} else if (now.isAfter(LocalTime.of(17, 0)) && now.isBefore(LocalTime.of(20, 0))) {
			newWeight = 12; // evening rush
		} else {
			newWeight = 3; // normal / night
		}

		Node c = graph.getNodeById("C");
		Node d = graph.getNodeById("D");

		if (c == null || d == null)
			return;

		for (Edge edge : graph.getEdges(c)) {
			if (edge.getTo().equals(d) && edge.getWeight() != newWeight) {
				edge.updateWeight(newWeight);
				graph.incrementVersion();

				System.out
						.println("[TRAFFIC UPDATE] C->D weight=" + newWeight + " | graphVersion=" + graph.getVersion());
			}
		}
	}

	/* ===================== ROUTE API ===================== */

	public RouteResponse getRoute(String from, String to) {

		long startTime = System.nanoTime();

		// Snapshot version for consistency
		int snapshotVersion = graph.getVersion();
		String cacheKey = "route:" + from + ":" + to + ":v" + snapshotVersion;

		// 1️⃣ Cache lookup
		String cachedJson = redisTemplate.opsForValue().get(cacheKey);
		if (cachedJson != null) {
			try {
				RouteResponse response = objectMapper.readValue(cachedJson, RouteResponse.class);

				metrics.recordHit();
				recordLatency(startTime);

				System.out.println("[CACHE HIT] key=" + cacheKey);
				return response;

			} catch (Exception e) {
				// fall through to recompute
				e.printStackTrace();
			}
		}

		// Cache miss
		metrics.recordMiss();
		System.out.println("[CACHE MISS] key=" + cacheKey);

		// 2️⃣ Compute using A*
		Node source = getNodeOrFail(from);
		Node destination = getNodeOrFail(to);

		AStar.Result result = AStar.findPath(graph, source, destination);

		RouteResponse response = new RouteResponse(result.distance(), result.path().stream().map(Node::getId).toList());

		// 3️⃣ Store in Redis
		try {
			String json = objectMapper.writeValueAsString(response);
			redisTemplate.opsForValue().set(cacheKey, json, 300, TimeUnit.SECONDS);
		} catch (Exception e) {
			// ignore cache write failure
		}

		recordLatency(startTime);
		System.out.println("[COMPUTED] key=" + cacheKey);

		return response;
	}

	/* ===================== HELPERS ===================== */

	private Node getNodeOrFail(String id) {
		Node node = graph.getNodeById(id);
		if (node == null) {
			throw new IllegalArgumentException("Invalid node: " + id);
		}
		return node;
	}

	private void recordLatency(long startTime) {
		long endTime = System.nanoTime();
		metrics.recordLatency((endTime - startTime) / 1_000_000);
	}
}
