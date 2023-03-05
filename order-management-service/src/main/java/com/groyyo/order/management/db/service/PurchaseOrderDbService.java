package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.PurchaseOrder;

public interface PurchaseOrderDbService extends AbstractJpaService<PurchaseOrder, Long> {


    List<PurchaseOrder> getAllPurchaseOrders();


    List<PurchaseOrder> getAllPurchaseOrdersForStatus(boolean status);


    PurchaseOrder getPurchaseOrderById(String id);


    PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);


    PurchaseOrder activateDeactivatePurchaseOrder(PurchaseOrder purchaseOrder, boolean status);


    boolean isEntityExistsByName(String name);
}
