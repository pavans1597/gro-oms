package com.groyyo.order.management.db.service;

import java.util.Date;
import java.util.List;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.PurchaseOrder;

public interface PurchaseOrderDbService extends AbstractJpaService<PurchaseOrder, Long> {

	List<PurchaseOrder> getAllPurchaseOrders(String factoryId);

	List<PurchaseOrder> getAllPurchaseOrdersForStatus(boolean status, String factoryId);

	PurchaseOrder getPurchaseOrderById(String id);

	PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);

	PurchaseOrder activateDeactivatePurchaseOrder(PurchaseOrder purchaseOrder, boolean status);

	boolean isEntityExistsByName(String name);

	/**
	 * @param purchaseOrderNumber
	 * @param factoryId
	 * @return
	 */
	Boolean existsByNameAndFactoryId(String purchaseOrderNumber, String factoryId);

	boolean isEntityExistsByNameAndFactoryId(String name, String factoryId);

	List<PurchaseOrder> findByFactoryIdAndPurchaseOrderStatus(String factoryId, List<PurchaseOrderStatus> status);

	List<PurchaseOrder> getAllPurchaseOrdersDateWise(Boolean status, String factoryId, Date startDate, Date endDate);
}
