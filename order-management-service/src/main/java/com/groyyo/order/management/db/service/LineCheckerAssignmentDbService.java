package com.groyyo.order.management.db.service;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.order.management.entity.LineCheckerAssignment;

import java.util.List;

public interface LineCheckerAssignmentDbService {

	List<LineCheckerAssignment> getAllLineCheckerAssignments(String factoryId);

	List<LineCheckerAssignment> getAllLineCheckerAssignmentsForStatus(boolean status,String factoryId);

	LineCheckerAssignment getLineCheckerAssignmentById(String id);

	LineCheckerAssignment saveLineCheckerAssignment(LineCheckerAssignment lineCheckerAssignment);

	LineCheckerAssignment activateDeactivateLineCheckerAssignment(LineCheckerAssignment lineCheckerAssignment, boolean status);

	List<LineCheckerAssignment> saveAllLineCheckerAssignemnt(List<LineCheckerAssignment> lineCheckerAssignments);

	/**
	 * @return
	 */
	List<LineCheckerAssignment> getLineCheckerAssignmentForPurchaseOrder(String purchaseOrderId,String factoryId);

	Long countLineCheckerByfactoryId(String factoryId, LineType productionLine,boolean status);
}
