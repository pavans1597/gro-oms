package com.groyyo.order.management.repository;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.PurchaseOrder;

@Repository
public interface PurchaseOrderRepository extends AbstractJpaRepository<PurchaseOrder, Long> {

	List<PurchaseOrder> findAllByFactoryId(String factoryId);

	List<PurchaseOrder> findByStatusAndFactoryId(Boolean status, String factoryId);

	PurchaseOrder findByName(String name);

	PurchaseOrder findByNameAndStatus(String name, Boolean status);

	PurchaseOrder findByNameAndFactoryId(String name, String factoryId);

	Boolean existsByNameAndFactoryId(String purchaseOrderNumber, String factoryId);

	List<PurchaseOrder> findByFactoryIdAndPurchaseOrderStatusIn(String factoryId, List<PurchaseOrderStatus> status);

	List<PurchaseOrder> findByFactoryIdAndStatusAndCreatedAtBetween(String factoryId, Boolean statusFilter, Date startDate, Date endDate);
}
