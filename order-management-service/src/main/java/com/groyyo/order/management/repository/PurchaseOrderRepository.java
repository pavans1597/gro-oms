package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.enums.PurchaseOrderStatus;
import org.springframework.stereotype.Repository;


@Repository
public interface PurchaseOrderRepository extends AbstractJpaRepository<PurchaseOrder, Long> {

    PurchaseOrder findByName(String name);

    PurchaseOrder findByNameAndStatus(String name, Boolean status);

//    Long countByPurchaseOrderStatusAndFactoryId(PurchaseOrderStatus purchaseOrderStatus, String factoryId);

    Long countByPurchaseOrderStatus(PurchaseOrderStatus purchaseOrderStatus);

    Long countByPurchaseOrderStatusAndStatus(PurchaseOrderStatus purchaseOrderStatus, boolean b);

//    Long countByFactoryId(String factoryId);
}
