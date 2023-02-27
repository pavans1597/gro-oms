package com.groyyo.order.management.service.repository;

import com.groyyo.order.management.service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
