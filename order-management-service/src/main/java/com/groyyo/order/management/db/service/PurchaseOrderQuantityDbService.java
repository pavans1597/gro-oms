package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.PurchaseOrderQuantity;

public interface PurchaseOrderQuantityDbService extends AbstractJpaService<PurchaseOrderQuantity, Long> {


    List<PurchaseOrderQuantity> getAllPurchaseOrderQuantitiesForPurchaseOrder(String purchaseOrderId);


    PurchaseOrderQuantity getPurchaseOrderQuantityById(String id);


    PurchaseOrderQuantity savePurchaseOrderQuantity(PurchaseOrderQuantity purchaseOrderQuantity);

    List<PurchaseOrderQuantity> savePurchaseOrderQuantityList(List<PurchaseOrderQuantity> purchaseOrderQuantityList);
    boolean isEntityExistsByName(String name);
}
