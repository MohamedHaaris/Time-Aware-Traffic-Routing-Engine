package com.traffic.routing.graph;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {

    // 🔹 Node registry (ID → Node)
    private final Map<String, Node> nodes = new HashMap<>();

    // 🔹 Adjacency list (Node → Edges)
    private final Map<Node, List<Edge>> adjList = new HashMap<>();

    private int version = 1;

    /* ===================== NODE MANAGEMENT ===================== */

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        adjList.putIfAbsent(node, new ArrayList<>());
    }

    public Node getNodeById(String id) {
        return nodes.get(id);
    }


public Set<Node> getNodes() {
    return nodes.values().stream().collect(Collectors.toSet());
}

    /* ===================== EDGE MANAGEMENT ===================== */

    public void addEdge(Node from, Node to, int weight) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Edge nodes cannot be null");
        }
        adjList.get(from).add(new Edge(to, weight));
    }

    public List<Edge> getEdges(Node node) {
        return adjList.getOrDefault(node, Collections.emptyList());
    }

    /* ===================== VERSIONING ===================== */

    public int getVersion() {
        return version;
    }

    public void incrementVersion() {
        version++;
    }
}
