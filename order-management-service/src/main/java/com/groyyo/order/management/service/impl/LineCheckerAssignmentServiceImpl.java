package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.PreconditionFailedException;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderResponseDto;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.core.dto.PurchaseOrder.UserLineDetails;
import com.groyyo.core.dto.userservice.LineResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.core.enums.QcUserType;
import com.groyyo.core.user.client.api.UserClientApi;
import com.groyyo.order.management.adapter.LineCheckerAssignmentAdapter;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.db.service.PurchaseOrderDbService;
import com.groyyo.order.management.dto.request.LineCheckerAssignmentRequestDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.kafka.publisher.PurchaseOrderPublisher;
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
	private PurchaseOrderDbService purchaseOrderDbService;

	@Autowired
	private LineCheckerAssignmentDbService lineCheckerAssignmentDbService;

	@Autowired
	private PurchaseOrderPublisher purchaseOrderPublisher;

	public ResponseDto<List<UserResponseDto>> getLineUsers(String factoryId, LineType lineType) {
		try {
			return userClientApi.getUsers(factoryId, lineType, QcUserType.CHECKER);
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

			PurchaseOrderResponseDto purchaseOrderResponseDto = purchaseOrderService.getPurchaseOrderById(purchaseOrderId);

			String salesOrderId = lineCheckerAssignmentRequestDto.getSalesOrderId();

			List<UserLineDetails> assignments = lineCheckerAssignmentRequestDto.getAssignment();

			for (UserLineDetails userLineDetails : assignments) {
				LineCheckerAssignment lineCheckerAssignment = LineCheckerAssignmentAdapter.buildLineCheckerAssignmentFromRequest(userLineDetails, purchaseOrderId, salesOrderId, factoryId);
				lineCheckerAssignments.add(lineCheckerAssignment);
			}

			if (Objects.nonNull(purchaseOrderResponseDto) && CollectionUtils.isNotEmpty(lineCheckerAssignments)) {

				lineCheckerAssignments = lineCheckerAssignmentDbService.saveAllLineCheckerAssignemnt(lineCheckerAssignments);

				/*
				 * Keeping forceUpdate true for now. Once the system will be stabilized, we will
				 * change it to false
				 */
				purchaseOrderService.changeStatusOfPurchaseOrder(purchaseOrderId, PurchaseOrderStatus.ONGOING, Boolean.TRUE);

				publishQcTaskAssignment(purchaseOrderId);

			}

			return lineCheckerAssignments;

		} catch (Exception e) {

			log.error("Exception occured while Line Assignment  ", e);
		}

		return lineCheckerAssignments;
	}

	private void publishQcTaskAssignment(String purchaseOrderId) {

		PurchaseOrderResponseDto purchaseOrderResponseDto = purchaseOrderService.getPurchaseOrderById(purchaseOrderId);

		log.info("Going to publish purchase order dto: {}", purchaseOrderResponseDto);

		purchaseOrderPublisher.publishQcTaskAssignment(purchaseOrderResponseDto);
	}

	@Override
	public List<LineCheckerAssignment> disableLineAssignmentsOnOrderCompletion(String purchaseOrderId) {

		String errorMsg = "";

		PurchaseOrder purchaseOrder = purchaseOrderDbService.getPurchaseOrderById(purchaseOrderId);

		if (Objects.isNull(purchaseOrder)) {
			errorMsg = "PurchaseOrder with id: " + purchaseOrderId + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		PurchaseOrderStatus purchaseOrderStatus = purchaseOrder.getPurchaseOrderStatus();

		if (Objects.nonNull(purchaseOrderStatus) && PurchaseOrderStatus.COMPLETED != purchaseOrderStatus) {
			errorMsg = "PurchaseOrder with id: " + purchaseOrderId + " is not completed yet. Please mark it completed first to disable assignments ";
			throw new PreconditionFailedException(errorMsg);
		}

		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		List<LineCheckerAssignment> lineCheckerAssignments = lineCheckerAssignmentDbService.getLineCheckerAssignmentForPurchaseOrderAndFactoryIdAndStatus(purchaseOrderId, factoryId, Boolean.TRUE);

		if (CollectionUtils.isEmpty(lineCheckerAssignments)) {
			errorMsg = "No Line Checker Assignments found for purchase order id: " + purchaseOrderId + " and factory id: " + factoryId;
			throw new NoRecordException(errorMsg);
		}

		return lineCheckerAssignmentDbService.activateDeactivateLineCheckerAssignments(lineCheckerAssignments, Boolean.FALSE);
	}
}
