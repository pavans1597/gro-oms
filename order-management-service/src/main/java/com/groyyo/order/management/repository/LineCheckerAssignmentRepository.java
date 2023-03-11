package com.groyyo.order.management.repository;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.LineCheckerAssignment;

import java.util.List;

public interface LineCheckerAssignmentRepository extends AbstractJpaRepository<LineCheckerAssignment, Long> {

	public List<LineCheckerAssignment> findAllByPurchaseOrderId(String purchaseOrderId);

	Long countByFactoryIdAndLineType(String factoryId, LineType lineType);

	Long countByFactoryIdAndLineTypeAndStatus(String factoryId, LineType lineType, boolean status);
}
