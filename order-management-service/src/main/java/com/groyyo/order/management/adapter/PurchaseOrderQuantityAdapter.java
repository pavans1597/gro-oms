package com.groyyo.order.management.adapter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderQuantityResponseDto;
import com.groyyo.order.management.dto.request.PurchaseOrderQuantityCreateDto;
import com.groyyo.order.management.dto.request.PurchaseOrderQuantityRequestDto;
import com.groyyo.order.management.entity.PurchaseOrderQuantity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PurchaseOrderQuantityAdapter {

	public PurchaseOrderQuantity buildPurchaseOrderQuantityFromRequest(PurchaseOrderQuantityRequestDto purchaseOrderQuantityRequest, String purchaseOrderId, Double tolerance, String factoryId) {

		Long quantity = purchaseOrderQuantityRequest.getQuantity();

		tolerance = Objects.isNull(tolerance) ? 0.0d : tolerance;

		Long targetQuantity = Objects.nonNull(quantity) ? (long) (quantity + (quantity * tolerance) / 100) : 0L;

		return PurchaseOrderQuantity
				.builder()
				.name(purchaseOrderId)
				.purchaseOrderId(purchaseOrderId)
				.quantity(quantity)
				.sizeId(purchaseOrderQuantityRequest.getSizeId())
				.sizeName(purchaseOrderQuantityRequest.getSizeName())
				.sizeGroupId(purchaseOrderQuantityRequest.getSizeGroupId())
				.sizeGroupName(purchaseOrderQuantityRequest.getSizeGroupName())
				.colourId(purchaseOrderQuantityRequest.getColourId())
				.colourName(purchaseOrderQuantityRequest.getColorName())
				.targetQuantity(targetQuantity)
				.factoryId(factoryId)
				.build();
	}

	public List<PurchaseOrderQuantity> buildPurchaseOrderQuantityListFromRequestList(List<PurchaseOrderQuantityRequestDto> purchaseOrderQuantityRequest, String purchaseOrderId, Double tolerance,
			String factoryId) {

		return purchaseOrderQuantityRequest.stream().map(purchaseOrderQuantity -> buildPurchaseOrderQuantityFromRequest(purchaseOrderQuantity, purchaseOrderId, tolerance, factoryId))
				.collect(Collectors.toList());
	}

	public PurchaseOrderQuantity buildPurchaseOrderQuantityFromResponse(PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto, String factoryId) {

		return PurchaseOrderQuantity
				.builder()
				.name(purchaseOrderQuantityResponseDto.getName())
				.factoryId(factoryId)
				.build();
	}

	public PurchaseOrderQuantity clonePurchaseOrderQuantityWithRequest(PurchaseOrderQuantityCreateDto purchaseOrderQuantityCreateDto, PurchaseOrderQuantity purchaseOrderQuantity) {

		if (StringUtils.isNotBlank(purchaseOrderQuantityCreateDto.getName()))
			purchaseOrderQuantity.setName(purchaseOrderQuantityCreateDto.getName());

		return purchaseOrderQuantity;
	}

	public PurchaseOrderQuantityRequestDto buildRequestFromResponse(PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto) {

		PurchaseOrderQuantityRequestDto purchaseOrderQuantityRequestDto = PurchaseOrderQuantityRequestDto.builder().build();

//        if (StringUtils.isNotBlank(purchaseOrderQuantityResponseDto.getName()))
//            purchaseOrderQuantityRequestDto.setName(purchaseOrderQuantityResponseDto.getName());

		return purchaseOrderQuantityRequestDto;
	}

	public PurchaseOrderQuantityResponseDto buildResponseFromEntity(PurchaseOrderQuantity purchaseOrderQuantity) {

		return PurchaseOrderQuantityResponseDto
				.builder()
				.name(purchaseOrderQuantity.getName())
				.id(purchaseOrderQuantity.getUuid())
				.purchaseOrderId(purchaseOrderQuantity.getPurchaseOrderId())
				.quantity(purchaseOrderQuantity.getQuantity())
				.sizeId(purchaseOrderQuantity.getSizeId())
				.sizeName(purchaseOrderQuantity.getSizeName())
				.sizeGroupId(purchaseOrderQuantity.getSizeGroupId())
				.sizeGroupName(purchaseOrderQuantity.getSizeGroupName())
				.colourId(purchaseOrderQuantity.getColourId())
				.colorName(purchaseOrderQuantity.getColourName())
				.targetQuantity(purchaseOrderQuantity.getTargetQuantity())
				.factoryId(purchaseOrderQuantity.getFactoryId())
				.build();
	}

	public List<PurchaseOrderQuantityResponseDto> buildResponsesListFromEntities(List<PurchaseOrderQuantity> purchaseOrderQuantity) {

		return purchaseOrderQuantity.stream().map(PurchaseOrderQuantityAdapter::buildResponseFromEntity).collect(Collectors.toList());
	}

}
