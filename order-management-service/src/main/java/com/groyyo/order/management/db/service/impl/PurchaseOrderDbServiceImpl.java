package com.groyyo.order.management.db.service.impl;

import com.groyyo.core.base.exception.GroyyoException;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.PurchaseOrderDbService;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.repository.PurchaseOrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderDbServiceImpl extends AbstractJpaServiceImpl<PurchaseOrder, Long, PurchaseOrderRepository> implements PurchaseOrderDbService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private LineCheckerAssignmentDbServiceImpl lineCheckerAssignmentDbServiceImpl;

    @Override
    protected PurchaseOrderRepository getJpaRepository() {
        return purchaseOrderRepository;
    }

    @Override
    public List<PurchaseOrder> getAllPurchaseOrders(String factoryId) {
        return (StringUtils.isNotBlank(factoryId) ? purchaseOrderRepository.findAllByFactoryId(factoryId) : purchaseOrderRepository.findAll());
    }

    @Override
    public List<PurchaseOrder> getAllPurchaseOrdersForStatus(boolean status, String factoryId) {
        return (StringUtils.isNotBlank(factoryId) ? purchaseOrderRepository.findByStatusAndFactoryId(status, factoryId) : purchaseOrderRepository.findByStatus(status));
    }

    @Override
    public PurchaseOrder getPurchaseOrderById(String id) {
        return purchaseOrderRepository.findByUuid(id);
    }

    @Override
    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        try {
            return purchaseOrderRepository.saveAndFlush(purchaseOrder);
        } catch (Exception e) {
            throw new GroyyoException("Something went wrong!");
        }
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
    public Boolean existsByNameAndFactoryId(String purchaseOrderNumber, String factoryId) {
        return purchaseOrderRepository.existsByNameAndFactoryId(purchaseOrderNumber, factoryId);
    }

    @Override
    public boolean isEntityExistsByNameAndFactoryId(String name, String factoryId) {
        return Objects.nonNull(purchaseOrderRepository.findByNameAndFactoryId(name, factoryId));
    }

    @Override
    public List<PurchaseOrder> findByFactoryIdAndPurchaseOrderStatus(String factoryId, List<PurchaseOrderStatus> status) {
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findByFactoryIdAndPurchaseOrderStatusIn(factoryId, status);
        purchaseOrderList.stream().filter(po -> po.getPurchaseOrderStatus().equals(PurchaseOrderStatus.YET_TO_START)).forEach(pol -> pol.setAssignedWithColours(Boolean.TRUE));
        List<LineCheckerAssignment> assignmentsForStatus = lineCheckerAssignmentDbServiceImpl.getAllLineCheckerAssignmentsForStatus(Boolean.TRUE, factoryId);
        Map<String, String> assignments = assignmentsForStatus.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        LineCheckerAssignment::getPurchaseOrderId,
                        assignment -> StringUtils.defaultIfBlank(assignment.getColourName(), ""),
                        StringUtils::defaultIfBlank
                ));

         purchaseOrderList.forEach(poObj -> {
             if(poObj.getPurchaseOrderStatus().equals(PurchaseOrderStatus.ONGOING)){
                 String colour = assignments.get(poObj.getUuid());
                 poObj.setAssignedWithColours(StringUtils.isNotBlank(colour));
             }
        } );

        if (purchaseOrderList == null) {
            return new ArrayList<>();
        }
        return purchaseOrderList;
    }

    @Override
    public List<PurchaseOrder> getAllPurchaseOrdersDateWise(Boolean statusFilter, String factoryId, Date startDate, Date endDate, PurchaseOrderStatus purchaseOrderStatus) {
        if (factoryId == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("factoryId, startDate, and endDate parameters cannot be null.");
        }
        if (Objects.nonNull(purchaseOrderStatus)) {
            return purchaseOrderRepository.findByFactoryIdAndPurchaseOrderStatusAndStatusAndCreatedAtLessThanEqual(factoryId, purchaseOrderStatus, statusFilter, endDate);
        } else {
            return purchaseOrderRepository.findByFactoryIdAndStatusAndCreatedAtBetween(factoryId, statusFilter, startDate, endDate);
        }
    }

}
