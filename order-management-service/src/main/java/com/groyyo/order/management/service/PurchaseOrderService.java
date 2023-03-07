package com.groyyo.order.management.service;

import java.util.List;

import com.groyyo.core.base.common.dto.PageResponse;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.response.PurchaseOrderResponseDto;

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
	PageResponse<PurchaseOrderResponseDto> getPurchaseOrderListing(int page, int limit);

}
