package com.groyyo.order.management.service.impl;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.core.kafka.dto.KafkaDTO;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.core.user.client.api.UserClientApi;
import com.groyyo.order.management.adapter.LineCheckerAssignmentAdapter;
import com.groyyo.order.management.constants.KafkaConstants;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.db.service.PurchaseOrderDbService;
import com.groyyo.order.management.db.service.PurchaseOrderQuantityDbService;
import com.groyyo.order.management.dto.request.LineCheckerAssignmentRequestDto;
import com.groyyo.order.management.dto.request.UserLineDetails;
import com.groyyo.order.management.dto.response.PurchaseOrderResponseDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.enums.PurchaseOrderStatus;
import com.groyyo.order.management.service.LineCheckerAssignmentService;
import com.groyyo.order.management.service.PurchaseOrderService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class LineCheckerAssignmentServiceImpl implements LineCheckerAssignmentService {

	@Autowired
	private UserClientApi userClientApi;

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Value("${kafka.quality-management.topic}")
	private String kafkaQualityManagementUpdatesTopic;

	@Autowired
	private LineCheckerAssignmentDbService lineCheckerAssignmentDbService;

	@Autowired
	private PurchaseOrderDbService purchaseOrderDbService;
	@Autowired
	private PurchaseOrderQuantityDbService purchaseOrderQuantityDbService;
	@Autowired
	private NotificationProducer notificationProducer;



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
			Optional<PurchaseOrder> purchaseOrderById = Optional.ofNullable(purchaseOrderDbService.getPurchaseOrderById(purchaseOrderId));
			if(purchaseOrderById.isPresent()){
				String salesOrderId = lineCheckerAssignmentRequestDto.getSalesOrderId();

			List<UserLineDetails> assignments = lineCheckerAssignmentRequestDto.getAssignment();

			for (UserLineDetails userLineDetails : assignments) {
				LineCheckerAssignment lineCheckerAssignment = LineCheckerAssignmentAdapter.buildLineCheckerAssignmentFromRequest(userLineDetails, purchaseOrderId, salesOrderId, factoryId);
				lineCheckerAssignments.add(lineCheckerAssignment);
			}

			if (CollectionUtils.isNotEmpty(lineCheckerAssignments)) {

				lineCheckerAssignments = lineCheckerAssignmentDbService.saveAllLineCheckerAssignemnt(lineCheckerAssignments);

				/*
				 * Keeping forceUpdate true for now. Once the system will be stabilized, we will
				 * change it to false
				 */
				purchaseOrderService.changeStatusOfPurchaseOrder(purchaseOrderId, PurchaseOrderStatus.ONGOING, Boolean.TRUE);
				publishQcTaskAssignment(KafkaConstants.KAFKA_QC_TASK_ASSIGNMENT_TYPE, KafkaConstants.KAFKA_QC_TASK_ASSIGNMENT_SUBTYPE_CREATE,purchaseOrderById.get().getUuid());

			}

				}
				return lineCheckerAssignments;

		} catch (Exception e) {

			log.error("Exception occured while Line Assignment  ", e);
		}

		return lineCheckerAssignments;
	}

	private void publishQcTaskAssignment(String kafkaQualityType, String kafkaQualitySubtype,String purchaseOrderUuid) {
		PurchaseOrderResponseDto purchaseOrderResponseDto = purchaseOrderService.getPurchaseOrderById(purchaseOrderUuid);
        KafkaDTO kafkaDTO = new KafkaDTO(kafkaQualityType, kafkaQualitySubtype, PurchaseOrderResponseDto.class.getName(), purchaseOrderResponseDto);
		notificationProducer.publish(kafkaQualityManagementUpdatesTopic, kafkaDTO.getClassName(), kafkaDTO);
		log.info("Qc Task Assignment Published ") ;
	}

}
