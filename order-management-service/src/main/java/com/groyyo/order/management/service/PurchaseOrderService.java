package com.groyyo.order.management.service;

import java.util.List;

import com.groyyo.core.base.common.dto.PageResponse;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderResponseDto;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.order.management.dto.filter.PurchaseOrderFilterDto;
import com.groyyo.order.management.dto.request.BulkPurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.request.dashboarddtos.AlterationCountResponseDto;
import com.groyyo.order.management.dto.request.dashboarddtos.CheckersCountResponseDto;
import com.groyyo.order.management.dto.request.dashboarddtos.OrdersCountResponseDto;
import com.groyyo.order.management.dto.request.dashboarddtos.QualityCountResponseDto;
import com.groyyo.order.management.dto.response.PurchaseOrderStatusCountDto;

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

    OrdersCountResponseDto getOrdersDetailsCounts(String factoryId, LineType linesType);

    CheckersCountResponseDto getCheckersDetailsCounts(String factoryId, LineType linesType);

    QualityCountResponseDto getQualityCheckDetailsCounts(String factoryId, LineType linesType);

    AlterationCountResponseDto getAlterationsCounts(String factoryId, LineType linesType);

    /**
     * @param status
     * @return
     */
    PurchaseOrderStatusCountDto getPurchaseOrderStatusCounts(Boolean status);

    PurchaseOrderResponseDto addBulkPurchaseOrder(List<BulkPurchaseOrderRequestDto> purchaseOrderRequestsDto);
}
