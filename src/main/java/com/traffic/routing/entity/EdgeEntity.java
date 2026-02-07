package com.traffic.routing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "edges")
public class EdgeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "from_node")
	private String fromNode;

	@Column(name = "to_node")
	private String toNode;

	@Column(name = "base_weight")
	private int baseWeight;

	public EdgeEntity() {
	}

	public EdgeEntity(String fromNode, String toNode, int baseWeight) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.baseWeight = baseWeight;
	}

	public String getFromNode() {
		return fromNode;
	}

	public String getToNode() {
		return toNode;
	}

	public int getBaseWeight() {
		return baseWeight;
	}
}
