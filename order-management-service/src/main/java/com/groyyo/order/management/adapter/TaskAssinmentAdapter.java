package com.groyyo.order.management.adapter;

import com.groyyo.core.dto.PurchaseOrder.TaskAssinmentRequestDto;
import com.groyyo.order.management.dto.request.UserLineDetails;
import com.groyyo.order.management.entity.PurchaseOrder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TaskAssinmentAdapter {

	public TaskAssinmentRequestDto buildTaskAssignmentFromRequest(PurchaseOrder purchaseOrder , UserLineDetails userLineDetails, String factoryId, String colorId, String sizeId ) {

		return TaskAssinmentRequestDto.builder()
				.assignedBy("currentUser")
//				.colorId(colorId)
				.qcStage(userLineDetails.getLineType().toString())
				.fabricId(purchaseOrder.getFabricId())
				.quantity(purchaseOrder.getTotalQuantity())
//				.sizeId(sizeId)
				.userId(userLineDetails.getUserId())
				.purchaseOrderId(purchaseOrder.getUuid())
				.status(purchaseOrder.isStatus())
				.factory(factoryId)
				.build();

	}
}
