package com.groyyo.order.management.service;

import com.groyyo.order.management.dto.request.PurchaseOrderQuantityCreateDto;
import com.groyyo.order.management.dto.request.PurchaseOrderQuantityRequestDto;
import com.groyyo.order.management.dto.response.PurchaseOrderQuantityResponseDto;

import java.util.List;

public interface PurchaseOrderQuantityService {

    List<PurchaseOrderQuantityResponseDto> getAllPurchaseOrderQuantitiesForPurchaseOrder(String purchaseOrderQuantityId);

    PurchaseOrderQuantityResponseDto getPurchaseOrderQuantityById(String id);

    PurchaseOrderQuantityResponseDto addPurchaseOrderQuantity(PurchaseOrderQuantityRequestDto purchaseOrderQuantityRequestDto, String purchaseOrderId, Double tolerance);

    List<PurchaseOrderQuantityResponseDto> addBulkPurchaseOrderQuantity(List<PurchaseOrderQuantityRequestDto> purchaseOrderQuantityRequestList, String purchaseOrderId, Double tolerance);
    
    PurchaseOrderQuantityResponseDto updatePurchaseOrderQuantity(PurchaseOrderQuantityCreateDto purchaseOrderQuantityCreateDto);
}

