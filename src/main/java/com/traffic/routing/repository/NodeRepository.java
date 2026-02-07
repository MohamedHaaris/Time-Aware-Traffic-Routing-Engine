package com.traffic.routing.repository;

import com.traffic.routing.entity.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeRepository extends JpaRepository<NodeEntity, String> {
}
