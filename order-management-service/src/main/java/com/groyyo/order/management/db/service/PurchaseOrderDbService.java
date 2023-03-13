package com.groyyo.order.management.db.service;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderDbService extends AbstractJpaService<PurchaseOrder, Long> {


    List<PurchaseOrder> getAllPurchaseOrders(String factoryId);


    List<PurchaseOrder> getAllPurchaseOrdersForStatus(boolean status,String factoryId);


    PurchaseOrder getPurchaseOrderById(String id);


    PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);


    PurchaseOrder activateDeactivatePurchaseOrder(PurchaseOrder purchaseOrder, boolean status);


    boolean isEntityExistsByName(String name);

}
