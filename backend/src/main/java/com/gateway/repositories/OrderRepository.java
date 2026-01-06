package com.gateway.repositories;

import com.gateway.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
    // Basic CRUD operations are built-in
}