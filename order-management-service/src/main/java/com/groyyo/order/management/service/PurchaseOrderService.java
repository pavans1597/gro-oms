package com.groyyo.order.management.service;

import com.groyyo.core.base.common.dto.PageResponse;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderResponseDto;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.order.management.dto.filter.PurchaseOrderFilterDto;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.response.PurchaseOrderStatusCountDto;

import java.util.List;

public interface PurchaseOrderService {

	List<PurchaseOrderResponseDto> getAllPurchaseOrders(Boolean status);

	PurchaseOrderResponseDto getPurchaseOrderById(String id);

	PurchaseOrderResponseDto addPurchaseOrder(PurchaseOrderRequestDto purchaseOrderRequestDto);

	PurchaseOrderResponseDto updatePurchaseOrder(PurchaseOrderUpdateDto purchaseOrderUpdateDto);

	/**
	 * @param
	 * @param limit
	 * @return
	 */
	PageResponse<PurchaseOrderResponseDto> getPurchaseOrderListing(PurchaseOrderFilterDto purchaseOrderFilterDto, PurchaseOrderStatus purchaseOrderStatus, int page, int limit);

	/**
	 * @param purchaseOrderId
	 * @param desiredPurchaseOrderStatus
	 * @param forceUpdate
	 */
	void changeStatusOfPurchaseOrder(String purchaseOrderId, PurchaseOrderStatus desiredPurchaseOrderStatus, Boolean forceUpdate);

	/**
	 * @param status
	 * @return
	 */
	PurchaseOrderStatusCountDto getPurchaseOrderStatusCounts(Boolean status);
}
