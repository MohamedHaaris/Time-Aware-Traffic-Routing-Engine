package com.traffic.routing.algorithm;

import com.traffic.routing.graph.*;

import java.util.*;

public class Dijkstra {

	public static Result shortestPath(Graph graph, Node source, Node destination) {

		Map<Node, Integer> dist = new HashMap<>();
		Map<Node, Node> parent = new HashMap<>();

		PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

		for (Node node : graph.getNodes()) {
			dist.put(node, Integer.MAX_VALUE);
		}

		dist.put(source, 0);
		pq.add(source);

		while (!pq.isEmpty()) {
			Node current = pq.poll();

			if (current.equals(destination))
				break;

			for (Edge edge : graph.getEdges(current)) {
				Node neighbor = edge.getTo();
				int newDist = dist.get(current) + edge.getWeight();

				if (newDist < dist.get(neighbor)) {
					dist.put(neighbor, newDist);
					parent.put(neighbor, current);
					pq.add(neighbor);
				}
			}
		}

		List<Node> path = buildPath(parent, source, destination);
		return new Result(dist.get(destination), path);
	}

	private static List<Node> buildPath(Map<Node, Node> parent, Node source, Node destination) {

		List<Node> path = new ArrayList<>();
		Node current = destination;

		while (current != null) {
			path.add(current);
			current = parent.get(current);
		}

		Collections.reverse(path);
		return path;
	}

	// Helper DTO inside algorithm (simple & clean)
	public record Result(int distance, List<Node> path) {
	}
}
