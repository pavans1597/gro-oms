package com.groyyo.order.management.service;

import com.groyyo.order.management.dto.request.LineAssignmentRequestDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;

import java.util.List;

public interface LineCheckerService {


//	List<UserResponseDto> getLineUsers(String factoryId, String departmentId, String keyword , LineType lineType);
//
//	List<LineResponseDto> getLines(String factoryId, LineType lineType);

	List<LineCheckerAssignment> lineCheckerAssignment(LineAssignmentRequestDto lineAssignmentRequestDto, String factoryId);
}
