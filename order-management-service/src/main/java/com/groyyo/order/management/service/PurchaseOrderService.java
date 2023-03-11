package com.groyyo.order.management.service;

import com.groyyo.core.base.common.dto.PageResponse;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.order.management.dto.filter.PurchaseOrderFilterDto;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.request.dashboarddtos.CheckersCountResponseDto;
import com.groyyo.order.management.dto.request.dashboarddtos.OrdersCountResponseDto;
import com.groyyo.order.management.dto.response.PurchaseOrderResponseDto;
import com.groyyo.order.management.enums.PurchaseOrderStatus;

import java.util.List;

public interface PurchaseOrderService {

	List<PurchaseOrderResponseDto> getAllPurchaseOrders(Boolean status);

	PurchaseOrderResponseDto getPurchaseOrderById(String id);

	PurchaseOrderResponseDto addPurchaseOrder(PurchaseOrderRequestDto purchaseOrderRequestDto);

	PurchaseOrderResponseDto updatePurchaseOrder(PurchaseOrderUpdateDto purchaseOrderUpdateDto);

	/**
	 * @param pageNumber
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

	OrdersCountResponseDto getOrdersDetailsCounts(String factoryId, LineType linesType);

	CheckersCountResponseDto getCheckersDetailsCounts(String factoryId, LineType linesType);
}
