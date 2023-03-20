package com.groyyo.order.management.service.impl;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.PreconditionFailedException;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderResponseDto;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.core.dto.PurchaseOrder.UserLineDetails;
import com.groyyo.core.user.client.api.UserClientApi;
import com.groyyo.core.user.dto.response.LineUserResponseDto;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

	@Override
	public List<LineUserResponseDto> getUsers(String factoryId, List<String> userIds) {
		log.info( "Serving Request received to fetch users for factoryId: {} and userIds: {}", factoryId, userIds);
		if(CollectionUtils.isEmpty(userIds)){
			throw new InputMismatchException("userIds can't empty");
		}
		List<LineCheckerAssignment>lineCheckerAssignments=lineCheckerAssignmentDbService.getLineCheckerAssignment(factoryId,userIds);
		if(CollectionUtils.isEmpty(lineCheckerAssignments)){
			log.info("No users are found with factoryId:{} and userIds:{}",factoryId,userIds);
			return new ArrayList<>();
		}
		Map<String,String>purchaseOrderIdToName=init(lineCheckerAssignments);
		return LineCheckerAssignmentAdapter.buildResponseDtoList(lineCheckerAssignments,purchaseOrderIdToName);
	}

	private Map<String,String> init(List<LineCheckerAssignment> lineCheckerAssignments){
		List<String> poIds = lineCheckerAssignments.stream()
				.map(LineCheckerAssignment::getPurchaseOrderId)
				.collect(Collectors.toList());
		List<PurchaseOrder> purchaseOrders = purchaseOrderDbService.findByUuidInAndStatus(poIds, true);
		return purchaseOrders.stream()
				.filter(purchaseOrder -> StringUtils.isNotBlank(purchaseOrder.getName()))
				.collect(Collectors.toMap(PurchaseOrder::getUuid, PurchaseOrder::getName));
	}
}
