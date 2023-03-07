package com.groyyo.order.management.service;

import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.response.PurchaseOrderResponseDto;

import java.util.List;



public interface PurchaseOrderService {

    List<PurchaseOrderResponseDto> getAllPurchaseOrders(Boolean status);

    PurchaseOrderResponseDto getPurchaseOrderById(String id);

    PurchaseOrderResponseDto addPurchaseOrder(PurchaseOrderRequestDto purchaseOrderRequestDto);

    PurchaseOrderResponseDto updatePurchaseOrder(PurchaseOrderUpdateDto purchaseOrderUpdateDto);

}
