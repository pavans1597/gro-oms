package com.groyyo.order.management.adapter;

import com.groyyo.order.management.dto.request.LineCheckerAssignmentRequestDto;
import com.groyyo.order.management.dto.request.UserLineDetails;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LineCheckerAdapter {

	public LineCheckerAssignment buildLineCheckerAssignmentFromRequest(LineCheckerAssignmentRequestDto lineCheckerAssignmentRequestDto) {

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

	public LineCheckerAssignment buildLineCheckerAssignmentFromRequest(UserLineDetails userLineDetails, String purchaseOrderId, String salesOrderId, String factoryId) {

		return LineCheckerAssignment
				.builder()
				.userName(userLineDetails.getUserName())
				.userId(userLineDetails.getUserId())
				.lineId(userLineDetails.getLineId())
				.lineType(userLineDetails.getLineType())
				.purchaseOrderId(purchaseOrderId)
				.salesOrderId(salesOrderId)
				.factoryId(factoryId)
				.build();

	}
}
