package com.groyyo.order.management.adapter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderResponseDto;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.entity.PurchaseOrder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PurchaseOrderAdapter {

	public PurchaseOrder buildPurchaseOrderFromRequest(PurchaseOrderRequestDto purchaseOrderRequestDto, String factoryId) {

		return PurchaseOrder
				.builder()
				.name(purchaseOrderRequestDto.getPurchaseOrderNumber())
				.styleId(Objects.nonNull(purchaseOrderRequestDto.getStyleRequestDto()) ? purchaseOrderRequestDto.getStyleRequestDto().getUuid() : null)
				.styleNumber(Objects.nonNull(purchaseOrderRequestDto.getStyleRequestDto()) ? purchaseOrderRequestDto.getStyleRequestDto().getStyleNumber() : null)
				.styleName(Objects.nonNull(purchaseOrderRequestDto.getStyleRequestDto()) ? purchaseOrderRequestDto.getStyleRequestDto().getName() : null)
				.fabricId(purchaseOrderRequestDto.getFabricId())
				.fabricName(purchaseOrderRequestDto.getFabricName())
				.buyerId(purchaseOrderRequestDto.getBuyerId())
				.buyerName(purchaseOrderRequestDto.getBuyerName())
				.tolerance(purchaseOrderRequestDto.getTolerance())
				.receiveDate(purchaseOrderRequestDto.getReceiveDate())
				.exFtyDate(purchaseOrderRequestDto.getExFtyDate())
				.seasonId(purchaseOrderRequestDto.getSeasonId())
				.seasonName(purchaseOrderRequestDto.getSeasonName())
				.fitId(purchaseOrderRequestDto.getFitId())
				.fitName(purchaseOrderRequestDto.getFitName())
				.partId(purchaseOrderRequestDto.getPartId())
				.partName(purchaseOrderRequestDto.getPartName())
				.productId(Objects.nonNull(purchaseOrderRequestDto.getStyleRequestDto()) ? purchaseOrderRequestDto.getStyleRequestDto().getProductId() : null)
				.productName(Objects.nonNull(purchaseOrderRequestDto.getStyleRequestDto()) ? purchaseOrderRequestDto.getStyleRequestDto().getProductName() : null)
				.factoryId(factoryId)
				.build();
	}

	public PurchaseOrder buildPurchaseOrderFromResponse(PurchaseOrderResponseDto purchaseOrderResponseDto, String factoryId) {

		return PurchaseOrder
				.builder()
				.name(purchaseOrderResponseDto.getName())
				.purchaseOrderStatus(purchaseOrderResponseDto.getPurchaseOrderStatus())
				.factoryId(factoryId)
				.build();
	}

	public PurchaseOrder clonePurchaseOrderWithRequest(PurchaseOrderRequestDto purchaseOrderRequestDto, PurchaseOrder purchaseOrder) {

		if (StringUtils.isNotBlank(purchaseOrderRequestDto.getPurchaseOrderNumber()))
			purchaseOrder.setName(purchaseOrderRequestDto.getPurchaseOrderNumber());

		return purchaseOrder;
	}

	public PurchaseOrderRequestDto buildRequestFromResponse(PurchaseOrderResponseDto purchaseOrderResponseDto) {

		PurchaseOrderRequestDto purchaseOrderRequestDto = PurchaseOrderRequestDto.builder().build();

		if (StringUtils.isNotBlank(purchaseOrderResponseDto.getName()))
			purchaseOrderRequestDto.setPurchaseOrderNumber(purchaseOrderResponseDto.getName());

		return purchaseOrderRequestDto;
	}

	public PurchaseOrderResponseDto buildResponseFromEntity(PurchaseOrder purchaseOrder) {

		return PurchaseOrderResponseDto
				.builder()
				.uuid(purchaseOrder.getUuid())
				.purchaseOrderNumber(purchaseOrder.getName())
				.purchaseOrderStatus(purchaseOrder.getPurchaseOrderStatus())
				.styleId(purchaseOrder.getStyleId())
				.styleNumber(purchaseOrder.getStyleNumber())
				.styleName(purchaseOrder.getStyleName())
				.fabricId(purchaseOrder.getFabricId())
				.fabricName(purchaseOrder.getFabricName())
				.buyerId(purchaseOrder.getBuyerId())
				.buyerName(purchaseOrder.getBuyerName())
				.tolerance(purchaseOrder.getTolerance())
				.receiveDate(purchaseOrder.getReceiveDate())
				.exFtyDate(purchaseOrder.getExFtyDate())
				.seasonId(purchaseOrder.getSeasonId())
				.seasonName(purchaseOrder.getSeasonName())
				.fitId(purchaseOrder.getFitId())
				.fitName(purchaseOrder.getFitName())
				.partId(purchaseOrder.getPartId())
				.partName(purchaseOrder.getPartName())
				.productId(purchaseOrder.getProductId())
				.productName(purchaseOrder.getProductName())
				.factoryId(purchaseOrder.getFactoryId())
				.build();
	}

	public List<PurchaseOrderResponseDto> buildResponsesFromEntities(List<PurchaseOrder> purchaseOrders) {

		return purchaseOrders.stream().map(PurchaseOrderAdapter::buildResponseFromEntity).collect(Collectors.toList());
	}

}
