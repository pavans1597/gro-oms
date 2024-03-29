package com.groyyo.order.management.adapter;

import com.groyyo.core.dto.PurchaseOrder.UserLineDetails;
import com.groyyo.core.user.dto.response.LineUserResponseDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	public static List<LineUserResponseDto> buildResponseDtoList(List<LineCheckerAssignment> lineCheckerAssignments, Map<String,String> purchaseOrderIdToName) {
		return lineCheckerAssignments.stream()
				.map(LineCheckerAssignmentAdapter::buildResponseDto)
				.peek(lineUserResponseDto -> {
					if (StringUtils.isNotBlank(lineUserResponseDto.getPurchaseOrderId())) {
						lineUserResponseDto.setPoName(purchaseOrderIdToName.getOrDefault(lineUserResponseDto.getPurchaseOrderId(), null));
					}
				})
				.collect(Collectors.toList());
	}

	private static LineUserResponseDto buildResponseDto(LineCheckerAssignment lineCheckerAssignment) {
		String departmentName = lineCheckerAssignment.getLineType().getLineTypeName().split(" ")[0];
		return LineUserResponseDto.builder()
				.id(lineCheckerAssignment.getUuid())
				.lineId(lineCheckerAssignment.getLineId())
				.lineName(lineCheckerAssignment.getLineName())
				.lineType(lineCheckerAssignment.getLineType())
				.factoryId(lineCheckerAssignment.getFactoryId())
				.userId(lineCheckerAssignment.getUserId())
				.userName(lineCheckerAssignment.getUserName())
				.departmentName(departmentName)
				.purchaseOrderId(lineCheckerAssignment.getPurchaseOrderId())
				.status(lineCheckerAssignment.isStatus())
				.build();
	}

}
