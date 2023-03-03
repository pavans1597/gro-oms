package com.groyyo.order.management.repository;

import com.groyyo.order.management.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
