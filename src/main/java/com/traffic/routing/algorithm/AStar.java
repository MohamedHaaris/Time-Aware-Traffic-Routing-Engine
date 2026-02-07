package com.traffic.routing.algorithm;

import com.traffic.routing.graph.*;

import java.util.*;

public class AStar {

	public static Result findPath(Graph graph, Node start, Node goal) {

		Map<Node, Integer> gScore = new HashMap<>();
		Map<Node, Integer> fScore = new HashMap<>();
		Map<Node, Node> parent = new HashMap<>();

		for (Node node : graph.getNodes()) {
			gScore.put(node, Integer.MAX_VALUE);
			fScore.put(node, Integer.MAX_VALUE);
		}

		gScore.put(start, 0);
		fScore.put(start, heuristic(start, goal));

		PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(fScore::get));
		openSet.add(start);

		while (!openSet.isEmpty()) {
			Node current = openSet.poll();

			if (current.equals(goal))
				break;

			for (Edge edge : graph.getEdges(current)) {
				Node neighbor = edge.getTo();
				int tentativeG = gScore.get(current) + edge.getWeight();

				if (tentativeG < gScore.get(neighbor)) {
					parent.put(neighbor, current);
					gScore.put(neighbor, tentativeG);
					fScore.put(neighbor, tentativeG + heuristic(neighbor, goal));
					openSet.add(neighbor);
				}
			}
		}

		List<Node> path = buildPath(parent, start, goal);
		return new Result(gScore.get(goal), path);
	}

	private static int heuristic(Node a, Node b) {
		return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
	}

	private static List<Node> buildPath(Map<Node, Node> parent, Node start, Node goal) {

		List<Node> path = new ArrayList<>();
		Node current = goal;

		while (current != null) {
			path.add(current);
			current = parent.get(current);
		}

		Collections.reverse(path);
		return path;
	}

	public record Result(int distance, List<Node> path) {
	}
}
