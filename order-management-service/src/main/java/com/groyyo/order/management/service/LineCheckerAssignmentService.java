package com.groyyo.order.management.service;

import com.groyyo.order.management.dto.request.LineCheckerAssignmentRequestDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;

import java.util.List;

public interface LineCheckerAssignmentService {

	List<LineCheckerAssignment> lineCheckerAssignment(LineCheckerAssignmentRequestDto lineCheckerAssignmentRequestDto, String factoryId);

	/**
	 * @param purchaseOrderId
	 * @return
	 */
	List<LineCheckerAssignment> disableLineAssignmentsOnOrderCompletion(String purchaseOrderId);
}
