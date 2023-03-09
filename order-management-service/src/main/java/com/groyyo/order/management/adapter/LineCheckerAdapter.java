package com.groyyo.order.management.adapter;

import com.groyyo.order.management.dto.request.LineAssignment;
import com.groyyo.order.management.dto.request.LineAssignmentRequestDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LineCheckerAdapter {

	public LineCheckerAssignment buildLineCheckerAssignmentFromRequest(LineAssignmentRequestDto lineAssignmentRequestDto) {

		return LineCheckerAssignment
				.builder()
//				.userName(lineAssignmentRequestDto.
//				.userId(checkerAssignDto.getUserId())
//				.lineId(checkerAssignDto.lin())
//				.lineType(checkerAssignDto.lin())
//				.userName(checkerAssignDto.getUserName())
//				.userName(checkerAssignDto.getUserName())
//				.hexCode(colorRequestDto.getHexCode())
//				.masterId(colorRequestDto.getMasterId())
				.build();
	}

	public LineCheckerAssignment buildLineCheckerAssignmentFromRequest(LineAssignment lineAssignment, String purchaseOrderId, String salesOrderId, String factoryId) {

		return LineCheckerAssignment
				.builder()
				.userName(lineAssignment.getUserName())
				.userId(lineAssignment.getUserId())
				.lineId(lineAssignment.getLineId())
				.lineType(lineAssignment.getLineType())
				.purchaseOrderId(purchaseOrderId)
				.salesOrderId(salesOrderId)
				.factoryId(factoryId)
				.build();

	}
}
