package com.groyyo.order.management.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.groyyo.order.management.dto.request.PurchaseOrderQuantityCreateDto;
import com.groyyo.order.management.dto.request.PurchaseOrderQuantityRequestDto;
import com.groyyo.order.management.dto.response.PurchaseOrderQuantityResponseDto;
import com.groyyo.order.management.service.PurchaseOrderQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groyyo.core.base.common.dto.ResponseDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/purchase/order/quantity")
public class PurchaseOrderQuantityController {

    @Autowired
    private PurchaseOrderQuantityService purchaseOrderQuantityService;

    @GetMapping("get/by/po/{purchaseOrderId}")
    public ResponseDto<List<PurchaseOrderQuantityResponseDto>> getAllPurchaseOrderQuantitiesForPurchaseOrder(@PathVariable String purchaseOrderId) {

        log.info("Request received to get all purchaseOrderQuantities");

        List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos = purchaseOrderQuantityService.getAllPurchaseOrderQuantitiesForPurchaseOrder(purchaseOrderId);

        return ResponseDto.success("Found " + purchaseOrderQuantityResponseDtos.size() + " purchaseOrderQuantities in the system", purchaseOrderQuantityResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<PurchaseOrderQuantityResponseDto> getPurchaseOrderQuantity(@PathVariable String id) {

        log.info("Request received to get purchaseOrderQuantity with id: {}", id);

        PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto = purchaseOrderQuantityService.getPurchaseOrderQuantityById(id);

        return Objects.isNull(purchaseOrderQuantityResponseDto) ? ResponseDto.failure("Found no purchaseOrderQuantity with id: " + id)
                : ResponseDto.success("Found purchaseOrderQuantity with id: " + id, purchaseOrderQuantityResponseDto);
    }


    @PostMapping("update")
    public ResponseDto<PurchaseOrderQuantityResponseDto> updatePurchaseOrderQuantity(@RequestBody PurchaseOrderQuantityCreateDto purchaseOrderQuantityCreateDto) {

        log.info("Request received to update purchaseOrderQuantity: {}", purchaseOrderQuantityCreateDto);

        PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto = purchaseOrderQuantityService.updatePurchaseOrderQuantity(purchaseOrderQuantityCreateDto);

        return Objects.isNull(purchaseOrderQuantityResponseDto) ? ResponseDto.failure("Unable to update purchaseOrderQuantity !!")
                : ResponseDto.success("PurchaseOrderQuantity updated successfully !!", purchaseOrderQuantityResponseDto);
    }
}

