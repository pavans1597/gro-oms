package com.groyyo.order.management.db.service.impl;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.PurchaseOrderQuantityDbService;
import com.groyyo.order.management.entity.PurchaseOrderQuantity;
import com.groyyo.order.management.repository.PurchaseOrderQuantityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class PurchaseOrderQuantityDbServiceImpl extends AbstractJpaServiceImpl<PurchaseOrderQuantity, Long, PurchaseOrderQuantityRepository> implements PurchaseOrderQuantityDbService {

    @Autowired
    private PurchaseOrderQuantityRepository purchaseOrderQuantityRepository;

    @Override
    protected PurchaseOrderQuantityRepository getJpaRepository() {
        return purchaseOrderQuantityRepository;
    }

    @Override
    public List<PurchaseOrderQuantity> getAllPurchaseOrderQuantitiesForPurchaseOrder(String purchaseOrderId) {
        return purchaseOrderQuantityRepository.findAllByPurchaseOrderId(purchaseOrderId);
    }

    @Override
    public PurchaseOrderQuantity getPurchaseOrderQuantityById(String id) {
        return purchaseOrderQuantityRepository.findByUuid(id);
    }

    @Override
    public PurchaseOrderQuantity savePurchaseOrderQuantity(PurchaseOrderQuantity purchaseOrderQuantity) {
        return purchaseOrderQuantityRepository.saveAndFlush(purchaseOrderQuantity);
    }

    @Override
    public List<PurchaseOrderQuantity> savePurchaseOrderQuantityList(List<PurchaseOrderQuantity> purchaseOrderQuantityList){
        return purchaseOrderQuantityRepository.saveAllAndFlush(purchaseOrderQuantityList);
    }

    @Override
    public boolean isEntityExistsByName(String name) {
        return Objects.nonNull(purchaseOrderQuantityRepository.findByName(name));
    }

    @Override
    public void getPurchaseOrderQuantityByPurchaseOrderId(String uuid) {
        return ;

    }
}
