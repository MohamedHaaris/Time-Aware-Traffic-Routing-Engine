package com.traffic.routing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "nodes")
public class NodeEntity {

    @Id
    private String id;

    private int x;
    private int y;

    // REQUIRED by JPA
    public NodeEntity() {}

    public NodeEntity(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
