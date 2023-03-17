package com.groyyo.order.management.service;

import java.util.List;

import com.groyyo.core.base.common.dto.PageResponse;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderResponseDto;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.order.management.dto.filter.PurchaseOrderFilterDto;
import com.groyyo.order.management.dto.request.BulkPurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.response.PurchaseOrderStatusCountDto;

public interface PurchaseOrderService {

    List<PurchaseOrderResponseDto> getAllPurchaseOrders(Boolean status);

    PurchaseOrderResponseDto getPurchaseOrderById(String id);

    PurchaseOrderResponseDto addPurchaseOrder(PurchaseOrderRequestDto purchaseOrderRequestDto);

    PurchaseOrderResponseDto updatePurchaseOrder(PurchaseOrderUpdateDto purchaseOrderUpdateDto);

    PageResponse<PurchaseOrderResponseDto> getPurchaseOrderListing(PurchaseOrderFilterDto purchaseOrderFilterDto, PurchaseOrderStatus purchaseOrderStatus, int page, int limit);

    void changeStatusOfPurchaseOrder(String purchaseOrderId, PurchaseOrderStatus desiredPurchaseOrderStatus, Boolean forceUpdate);

    PurchaseOrderStatusCountDto getPurchaseOrderStatusCounts(Boolean status);

    List<PurchaseOrderResponseDto> addBulkPurchaseOrder(List<BulkPurchaseOrderRequestDto> purchaseOrderRequestsDto);
}
