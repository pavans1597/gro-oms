package com.groyyo.order.management.db.service.impl;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.PurchaseOrderDbService;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class PurchaseOrderDbServiceImpl extends AbstractJpaServiceImpl<PurchaseOrder, Long, PurchaseOrderRepository> implements PurchaseOrderDbService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Override
    protected PurchaseOrderRepository getJpaRepository() {
        return purchaseOrderRepository;
    }

    @Override
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    @Override
    public List<PurchaseOrder> getAllPurchaseOrdersForStatus(boolean status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    @Override
    public PurchaseOrder getPurchaseOrderById(String id) {
        return purchaseOrderRepository.findByUuid(id);
    }

    @Override
    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.saveAndFlush(purchaseOrder);
    }

    @Override
    public PurchaseOrder activateDeactivatePurchaseOrder(PurchaseOrder purchaseOrder, boolean status) {
        purchaseOrder.setStatus(status);
        return purchaseOrderRepository.saveAndFlush(purchaseOrder);
    }

    @Override
    public boolean isEntityExistsByName(String name) {
        return Objects.nonNull(purchaseOrderRepository.findByName(name));
    }

    @Override
    public Long getCountByPurchaseOrderStatus(PurchaseOrderStatus purchaseOrderStatus, String factoryId, boolean b) {
        if(!Objects.isNull(factoryId)){
//            return purchaseOrderRepository.countByPurchaseOrderStatusAndFactoryId(purchaseOrderStatus,factoryId);
            return  0L ;
        }else {
            return purchaseOrderRepository.countByPurchaseOrderStatusAndStatus(purchaseOrderStatus,true);
        }

    }

    @Override
    public Long getTotalCount(String factoryId) {
        if(!Objects.isNull(factoryId)){
//            return purchaseOrderRepository.countByFactoryId(factoryId);
            return  0L ;
        }else {
            return purchaseOrderRepository.count();
        }
    }

}
