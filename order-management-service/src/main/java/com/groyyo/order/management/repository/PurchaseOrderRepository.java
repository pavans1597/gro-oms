package com.groyyo.order.management.repository;

import com.groyyo.order.management.entity.PurchaseOrder;
import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;


@Repository
public interface PurchaseOrderRepository extends AbstractJpaRepository<PurchaseOrder, Long> {

    PurchaseOrder findByName(String name);

    PurchaseOrder findByNameAndStatus(String name, Boolean status);

}
