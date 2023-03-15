package com.groyyo.order.management.repository;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.LineCheckerAssignment;

import java.util.ArrayList;
import java.util.List;

public interface LineCheckerAssignmentRepository extends AbstractJpaRepository<LineCheckerAssignment, Long> {

	List<LineCheckerAssignment> findAllByFactoryId(String factoryId);

	List<LineCheckerAssignment> findByStatusAndFactoryId(Boolean status, String factoryId);

	List<LineCheckerAssignment> findAllByPurchaseOrderId(String purchaseOrderId);
	
	List<LineCheckerAssignment> findAllByPurchaseOrderIdAndStatus(String purchaseOrderId, boolean status);

	Long countByFactoryIdAndLineType(String factoryId, LineType lineType);

	Long countByFactoryIdAndLineTypeAndStatus(String factoryId, LineType lineType, boolean status);

	List<LineCheckerAssignment> findAllByPurchaseOrderIdAndFactoryId(String purchaseOrderId, String factoryId);
	
	List<LineCheckerAssignment> findAllByPurchaseOrderIdAndFactoryIdAndStatus(String purchaseOrderId, String factoryId, boolean status);

	ArrayList<LineCheckerAssignment> findUserIdByFactoryIdAndLineTypeAndStatus(String factoryId, LineType lineType, boolean status);

}
