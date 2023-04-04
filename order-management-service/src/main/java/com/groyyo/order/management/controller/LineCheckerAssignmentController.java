/**
 * 
 */
package com.groyyo.order.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.user.dto.response.LineUserResponseDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.service.LineCheckerAssignmentService;

import lombok.extern.log4j.Log4j2;

/**
 * @author nipunaggarwal
 *
 */
@Log4j2
@RestController
@RequestMapping("line/assignment")
public class LineCheckerAssignmentController {

	@Autowired
	private LineCheckerAssignmentService lineCheckerAssignmentService;

	@PutMapping("disable/purchase/order/{purchaseOrderId}")
	public ResponseDto<List<LineCheckerAssignment>> disableLineAssignmentsOnOrderCompletion(@PathVariable String purchaseOrderId) {

		log.info("Request received to disable line assignments for purchase order with uuid: {} ", purchaseOrderId);

		List<LineCheckerAssignment> lineCheckerAssignments = lineCheckerAssignmentService.disableLineAssignmentsOnOrderCompletion(purchaseOrderId);

		return ResponseDto.success("Disabled line assignments for purchase order with id: " + purchaseOrderId, lineCheckerAssignments);
	}

	@GetMapping("fetch/users")
	public ResponseDto<List<LineUserResponseDto>> getUsers(@RequestParam(value = "factoryId", required = true) String factoryId,
			@RequestParam(value = "userIds", required = true) List<String> userIds) {
		log.info("Request received to fetch users for factoryId: {} and userIds: {}", factoryId, userIds);

		List<LineUserResponseDto> lineUserResponseDto = lineCheckerAssignmentService.getUsers(factoryId, userIds);

		return ResponseDto.success("Number of users found: " + lineUserResponseDto.size(), lineUserResponseDto);
	}

}
