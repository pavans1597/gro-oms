package com.groyyo.order.management.service;

import java.util.List;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.order.management.dto.request.LineCheckerAssignmentRequestDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;

public interface LineCheckerAssignmentService {

	ResponseDto<List<UserResponseDto>> getLineUsers(String factoryId, LineType lineType);

	ResponseDto<List<LineResponseDto>> getLines(String factoryId, LineType lineType);

	List<LineCheckerAssignment> lineCheckerAssignment(LineCheckerAssignmentRequestDto lineCheckerAssignmentRequestDto, String factoryId);

	/**
	 * @param purchaseOrderId
	 * @return
	 */
	List<LineCheckerAssignment> disableLineAssignmentsOnOrderCompletion(String purchaseOrderId);
}
