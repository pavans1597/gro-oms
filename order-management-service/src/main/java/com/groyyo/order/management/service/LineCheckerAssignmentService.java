package com.groyyo.order.management.service;

import java.util.List;

import com.groyyo.core.user.dto.response.LineUserResponseDto;
import com.groyyo.order.management.dto.request.LineCheckerAssignmentRequestDto;
import com.groyyo.order.management.dto.response.PurchaseOrderAndLineColourResponse;
import com.groyyo.order.management.entity.LineCheckerAssignment;

public interface LineCheckerAssignmentService {

	List<LineCheckerAssignment> lineCheckerAssignment(LineCheckerAssignmentRequestDto lineCheckerAssignmentRequestDto, String factoryId);

	/**
	 * @param purchaseOrderId
	 * @return
	 */
	List<LineCheckerAssignment> disableLineAssignmentsOnOrderCompletion(String purchaseOrderId);

	List<LineUserResponseDto> getUsers(String factoryId, List<String> userIds);

	/**
	 * @param purchaseOrderId
	 */
	void publishQcTaskAssignment(String purchaseOrderId);

	PurchaseOrderAndLineColourResponse getLinesAndColourByPoId(String purchaseOrderId);
}
