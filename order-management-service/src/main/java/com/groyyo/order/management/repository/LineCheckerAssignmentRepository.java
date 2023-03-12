package com.groyyo.order.management.repository;

import java.util.List;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.LineCheckerAssignment;

public interface LineCheckerAssignmentRepository extends AbstractJpaRepository<LineCheckerAssignment, Long> {

	List<LineCheckerAssignment> findAllByFactoryId(String factoryId);

	List<LineCheckerAssignment> findByStatusAndFactoryId(Boolean status, String factoryId);

	List<LineCheckerAssignment> findAllByPurchaseOrderId(String purchaseOrderId);

	Long countByFactoryIdAndLineType(String factoryId, LineType lineType);

	Long countByFactoryIdAndLineTypeAndStatus(String factoryId, LineType lineType, boolean status);

	List<LineCheckerAssignment> findAllByPurchaseOrderIdAndFactoryId(String purchaseOrderId, String factoryId);

	Long countByLineTypeAndStatus(LineType lineType, boolean status);
}
