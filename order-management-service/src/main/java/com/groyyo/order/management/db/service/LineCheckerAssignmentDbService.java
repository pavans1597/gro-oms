package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.order.management.entity.LineCheckerAssignment;

public interface LineCheckerAssignmentDbService {

	List<LineCheckerAssignment> getAllLineCheckerAssignments(String factoryId);

	List<LineCheckerAssignment> getAllLineCheckerAssignmentsForStatus(boolean status, String factoryId);

	LineCheckerAssignment getLineCheckerAssignmentById(String id);

	LineCheckerAssignment saveLineCheckerAssignment(LineCheckerAssignment lineCheckerAssignment);

	LineCheckerAssignment activateDeactivateLineCheckerAssignment(LineCheckerAssignment lineCheckerAssignment, boolean status);

	List<LineCheckerAssignment> saveAllLineCheckerAssignemnt(List<LineCheckerAssignment> lineCheckerAssignments);

	long countLineCheckerByFactoryId(String factoryId, LineType lineType, boolean status);

	/**
	 * @return
	 */
	List<LineCheckerAssignment> getLineCheckerAssignmentForPurchaseOrder(String purchaseOrderId, String factoryId);

	/**
	 * @param lineCheckerAssignments
	 * @param status
	 * @return
	 */
	List<LineCheckerAssignment> activateDeactivateLineCheckerAssignments(List<LineCheckerAssignment> lineCheckerAssignments, boolean status);

	/**
	 * @param factoryId
	 * @param lineType
	 * @param status
	 * @return
	 */

}
