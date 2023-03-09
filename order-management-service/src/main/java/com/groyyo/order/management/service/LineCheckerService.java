package com.groyyo.order.management.service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.order.management.dto.request.LineAssignmentRequestDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;

import java.util.List;

public interface LineCheckerService {


	ResponseDto<List<UserResponseDto>> getLineUsers(String factoryId, LineType lineType);

	ResponseDto<List<LineResponseDto>> getLines(String factoryId, LineType lineType);

	List<LineCheckerAssignment> lineCheckerAssignment(LineAssignmentRequestDto lineAssignmentRequestDto, String factoryId);
}
