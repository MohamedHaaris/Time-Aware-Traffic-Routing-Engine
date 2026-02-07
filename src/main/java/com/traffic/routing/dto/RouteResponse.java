package com.traffic.routing.dto;

import java.io.Serializable;
import java.util.List;

public class RouteResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private int distance;
    private List<String> path;
    
    public RouteResponse() {}

    public RouteResponse(int distance, List<String> path) {
        this.distance = distance;
        this.path = path;
    }

    public int getDistance() {
        return distance;
    }

    public List<String> getPath() {
        return path;
    }
}
