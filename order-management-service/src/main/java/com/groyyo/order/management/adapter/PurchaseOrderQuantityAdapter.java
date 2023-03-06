package com.groyyo.order.management.adapter;

import java.util.List;
import java.util.stream.Collectors;

import com.groyyo.order.management.dto.request.PurchaseOrderQuantityCreateDto;
import com.groyyo.order.management.dto.request.PurchaseOrderQuantityRequestDto;
import com.groyyo.order.management.dto.response.PurchaseOrderQuantityResponseDto;
import org.apache.commons.lang3.StringUtils;
import com.groyyo.order.management.entity.PurchaseOrderQuantity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PurchaseOrderQuantityAdapter {

    public PurchaseOrderQuantity buildPurchaseOrderQuantityFromRequest(PurchaseOrderQuantityRequestDto purchaseOrderQuantityRequest, String purchaseOrderId, Double tolerance) {
        Long quantity = purchaseOrderQuantityRequest.getQuantity();
        Long targetQuantity = (long) (quantity + (quantity * tolerance) / 100);
        return PurchaseOrderQuantity
                .builder()
                .name(purchaseOrderId)
                .purchaseOrderId(purchaseOrderId)
                .quantity(quantity)
                .sizeId(purchaseOrderQuantityRequest.getSizeId())
                .colourId(purchaseOrderQuantityRequest.getColourId())
                .targetQuantity(targetQuantity)
                .build();
    }

    public List<PurchaseOrderQuantity> buildPurchaseOrderQuantityListFromRequestList(List<PurchaseOrderQuantityRequestDto> purchaseOrderQuantityRequest, String purchaseOrderId, Double tolerance) {
        return purchaseOrderQuantityRequest.stream().map(purchaseOrderQuantity -> buildPurchaseOrderQuantityFromRequest(purchaseOrderQuantity, purchaseOrderId, tolerance)).collect(Collectors.toList());
    }

    public PurchaseOrderQuantity buildPurchaseOrderQuantityFromResponse(PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto) {
        return PurchaseOrderQuantity
                .builder()
                .name(purchaseOrderQuantityResponseDto.getName())
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
                .purchaseOrderId(purchaseOrderQuantity.getPurchaseOrderId())
                .quantity(purchaseOrderQuantity.getQuantity())
                .sizeId(purchaseOrderQuantity.getSizeId())
                .colourId(purchaseOrderQuantity.getColourId())
                .targetQuantity(purchaseOrderQuantity.getTargetQuantity())
                .build();
    }

    public List<PurchaseOrderQuantityResponseDto> buildResponsesListFromEntities(List<PurchaseOrderQuantity> purchaseOrderQuantity) {

        return purchaseOrderQuantity.stream().map(PurchaseOrderQuantityAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}

