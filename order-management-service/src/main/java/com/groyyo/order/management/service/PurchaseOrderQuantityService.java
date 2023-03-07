package com.groyyo.order.management.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.groyyo.order.management.dto.request.PurchaseOrderQuantityCreateDto;
import com.groyyo.order.management.dto.request.PurchaseOrderQuantityRequestDto;
import com.groyyo.order.management.dto.response.PurchaseOrderQuantityResponseDto;

public interface PurchaseOrderQuantityService {

	List<PurchaseOrderQuantityResponseDto> getAllPurchaseOrderQuantitiesForPurchaseOrder(String purchaseOrderQuantityId);

	PurchaseOrderQuantityResponseDto getPurchaseOrderQuantityById(String id);

	PurchaseOrderQuantityResponseDto addPurchaseOrderQuantity(PurchaseOrderQuantityRequestDto purchaseOrderQuantityRequestDto, String purchaseOrderId, Double tolerance);

	List<PurchaseOrderQuantityResponseDto> addBulkPurchaseOrderQuantity(List<PurchaseOrderQuantityRequestDto> purchaseOrderQuantityRequestList, String purchaseOrderId, Double tolerance);

	PurchaseOrderQuantityResponseDto updatePurchaseOrderQuantity(PurchaseOrderQuantityCreateDto purchaseOrderQuantityCreateDto);

	/**
	 * @param purchaseOrderIds
	 * @return
	 */
	Map<String, List<PurchaseOrderQuantityResponseDto>> getQuantitiesForPurchaseOrders(List<String> purchaseOrderIds);

	/**
	 * @param purchaseOrderId
	 * @return
	 */
	Pair<Long, Long> getTotalQuantityAndTotalTargetQuantityForPurchaseOrder(String purchaseOrderId);

	/**
	 * @param purchaseOrderId
	 * @return
	 */
	Long getTotalQuantityForPurchaseOrder(String purchaseOrderId);

	/**
	 * @param purchaseOrderId
	 * @return
	 */
	Long getTotalTargetQuantityForPurchaseOrder(String purchaseOrderId);

	/**
	 * @param purchaseOrderIds
	 * @return
	 */
	Map<String, Pair<Long, Long>> getTotalQuantityAndTotalTargetQuantityForPurchaseOrders(List<String> purchaseOrderIds);

}
