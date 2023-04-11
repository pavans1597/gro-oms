package com.groyyo.order.management.db.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.PurchaseOrderDbService;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.repository.PurchaseOrderRepository;

@Service
public class PurchaseOrderDbServiceImpl extends AbstractJpaServiceImpl<PurchaseOrder, Long, PurchaseOrderRepository> implements PurchaseOrderDbService {

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

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
	public Boolean existsByNameAndFactoryId(String purchaseOrderNumber, String factoryId) {
		return purchaseOrderRepository.existsByNameAndFactoryId(purchaseOrderNumber, factoryId);
	}

	@Override
	public boolean isEntityExistsByNameAndFactoryId(String name, String factoryId) {
		return Objects.nonNull(purchaseOrderRepository.findByNameAndFactoryId(name, factoryId));
	}

	@Override
	public List<PurchaseOrder> findByFactoryIdAndPurchaseOrderStatus(String factoryId, List<PurchaseOrderStatus> status ) {
		List<PurchaseOrder>purchaseOrderList=purchaseOrderRepository.findByFactoryIdAndPurchaseOrderStatusIn(factoryId,status);
		if(purchaseOrderList==null){
			return new ArrayList<>();
		}
		return purchaseOrderList;
	}

}
