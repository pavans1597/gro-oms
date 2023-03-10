package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.core.user.client.api.UserClientApi;
import com.groyyo.order.management.adapter.LineCheckerAssignmentAdapter;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.dto.request.LineCheckerAssignmentRequestDto;
import com.groyyo.order.management.dto.request.UserLineDetails;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.enums.PurchaseOrderStatus;
import com.groyyo.order.management.service.LineCheckerAssignmentService;
import com.groyyo.order.management.service.PurchaseOrderService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class LineCheckerAssignmentServiceImpl implements LineCheckerAssignmentService {

	@Autowired
	private UserClientApi userClientApi;

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private LineCheckerAssignmentDbService lineCheckerAssignmentDbService;

	public ResponseDto<List<UserResponseDto>> getLineUsers(String factoryId, LineType lineType) {
		try {
			return userClientApi.getUsers(factoryId, lineType);
		} catch (Exception e) {
			log.error("exception occured while calling getLineUsers service ");
		}
		return null;
	}

	@Override
	public ResponseDto<List<LineResponseDto>> getLines(String factoryId, LineType lineType) {
		try {
			return userClientApi.getLines(factoryId);
		} catch (Exception e) {
			log.error("exception occured while calling getLines service ");
		}
		return null;
	}

	@Override
	public List<LineCheckerAssignment> lineCheckerAssignment(LineCheckerAssignmentRequestDto lineCheckerAssignmentRequestDto, String factoryId) {

		List<LineCheckerAssignment> lineCheckerAssignments = new ArrayList<>();

		try {

			String purchaseOrderId = lineCheckerAssignmentRequestDto.getPurchaseOrderId();
			String salesOrderId = lineCheckerAssignmentRequestDto.getSalesOrderId();

			List<UserLineDetails> assignments = lineCheckerAssignmentRequestDto.getAssignment();

			for (UserLineDetails userLineDetails : assignments) {
				LineCheckerAssignment lineCheckerAssignment = LineCheckerAssignmentAdapter.buildLineCheckerAssignmentFromRequest(userLineDetails, purchaseOrderId, salesOrderId, factoryId);
				lineCheckerAssignments.add(lineCheckerAssignment);
			}

			if (CollectionUtils.isNotEmpty(assignments)) {

				lineCheckerAssignments = lineCheckerAssignmentDbService.saveAllLineCheckerAssignemnt(lineCheckerAssignments);

				/*
				 * Keeping forceUpdate true for now. Once the system will be stabilized, we will
				 * change it to false
				 */
				purchaseOrderService.changeStatusOfPurchaseOrder(purchaseOrderId, PurchaseOrderStatus.ONGOING, Boolean.TRUE);
			}

		} catch (Exception e) {

			log.error("Exception occured while Line Assignment  ", e);
		}

		return lineCheckerAssignments;
	}

}
