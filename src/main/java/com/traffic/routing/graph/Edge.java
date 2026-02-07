package com.traffic.routing.graph;

public class Edge {
    private final Node to;
    private int weight; //mutable

    public Edge(Node to, int weight) {
        this.to = to;
        this.weight = weight;
    }

    public Node getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }
    
   public void updateWeight(int newWeight) {
	   this.weight=newWeight;
   }
}
