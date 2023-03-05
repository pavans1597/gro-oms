package com.groyyo.order.management.repository;

import com.groyyo.order.management.entity.PurchaseOrderQuantity;
import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;

import java.util.List;


@Repository
public interface PurchaseOrderQuantityRepository extends AbstractJpaRepository<PurchaseOrderQuantity, Long> {

    PurchaseOrderQuantity findByName(String name);

    PurchaseOrderQuantity findByNameAndStatus(String name, Boolean status);

    List<PurchaseOrderQuantity> findAllByPurchaseOrderId(String purchaseOrderId);

}
