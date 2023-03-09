package com.groyyo.order.management.adapter;

import java.util.List;
import java.util.stream.Collectors;

import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.response.PurchaseOrderResponseDto;
import org.apache.commons.lang3.StringUtils;
import com.groyyo.order.management.entity.PurchaseOrder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PurchaseOrderAdapter {

    public PurchaseOrder buildPurchaseOrderFromRequest(PurchaseOrderRequestDto purchaseOrderRequestDto) {
        return PurchaseOrder
                .builder()
                .name(purchaseOrderRequestDto.getPurchaseOrderNumber())
                .styleId(purchaseOrderRequestDto.getStyleRequestDto().getUuid())
                .styleNumber(purchaseOrderRequestDto.getStyleRequestDto().getStyleNumber())
                .styleName(purchaseOrderRequestDto.getStyleRequestDto().getName())
                .fabricId(purchaseOrderRequestDto.getFabricId())
                .fabricName(purchaseOrderRequestDto.getFabricName())
                .buyerId(purchaseOrderRequestDto.getBuyerId())
                .buyerName(purchaseOrderRequestDto.getBuyerName())
                .tolerance(purchaseOrderRequestDto.getTolerance())
                .receiveDate(purchaseOrderRequestDto.getReceiveDate())
                .exFtyDate(purchaseOrderRequestDto.getExFtyDate())
                .seasonId(purchaseOrderRequestDto.getSeasonId())
                .fitId(purchaseOrderRequestDto.getFitId())
                .partId(purchaseOrderRequestDto.getPartId())
                .productId(purchaseOrderRequestDto.getStyleRequestDto().getProductId())
                .productName(purchaseOrderRequestDto.getStyleRequestDto().getProductName())
                .build();
    }

    public PurchaseOrder buildPurchaseOrderFromResponse(PurchaseOrderResponseDto purchaseOrderResponseDto) {

        return PurchaseOrder
                .builder()
                .name(purchaseOrderResponseDto.getName())
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
                .fitId(purchaseOrder.getFitId())
                .partId(purchaseOrder.getPartId())
                .productId(purchaseOrder.getProductId())
                .productName(purchaseOrder.getProductName())
                .build();
    }

    public List<PurchaseOrderResponseDto> buildResponsesFromEntities(List<PurchaseOrder> purchaseOrders) {

        return purchaseOrders.stream().map(PurchaseOrderAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}
