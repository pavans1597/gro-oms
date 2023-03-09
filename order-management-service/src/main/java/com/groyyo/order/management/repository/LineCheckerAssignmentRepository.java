package com.groyyo.order.management.repository;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.LineCheckerAssignment;

public interface LineCheckerAssignmentRepository extends AbstractJpaRepository<LineCheckerAssignment, Long> {

	public List<LineCheckerAssignment> findAllByPurchaseOrderId(String purchaseOrderId);

}
