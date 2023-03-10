package com.groyyo.order.management.adapter;

import java.util.List;
import java.util.stream.Collectors;

import com.groyyo.order.management.dto.request.UserLineDetails;
import com.groyyo.order.management.entity.LineCheckerAssignment;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LineCheckerAssignmentAdapter {

	public UserLineDetails buildUserLineDetailsFromEntity(LineCheckerAssignment lineCheckerAssignment) {

		return UserLineDetails
				.builder()
				.userId(lineCheckerAssignment.getUserId())
				.userName(lineCheckerAssignment.getUserName())
				.lineId(lineCheckerAssignment.getLineId())
				.lineName(lineCheckerAssignment.getLineName())
				.lineType(lineCheckerAssignment.getLineType())
				.build();
	}

	public LineCheckerAssignment buildLineCheckerAssignmentFromRequest(UserLineDetails userLineDetails, String purchaseOrderId, String salesOrderId, String factoryId) {

		return LineCheckerAssignment
				.builder()
				.userName(userLineDetails.getUserName())
				.userId(userLineDetails.getUserId())
				.lineId(userLineDetails.getLineId())
				.lineName(userLineDetails.getLineName())
				.lineType(userLineDetails.getLineType())
				.purchaseOrderId(purchaseOrderId)
				.salesOrderId(salesOrderId)
				.factoryId(factoryId)
				.build();

	}

	public List<UserLineDetails> buildUserLineDetailsFromEntities(List<LineCheckerAssignment> lineCheckerAssignments) {

		return lineCheckerAssignments.stream().map(LineCheckerAssignmentAdapter::buildUserLineDetailsFromEntity).collect(Collectors.toList());
	}
}
