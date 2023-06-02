package com.groyyo.order.management.controller;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.PurchaseOrder.ColourQuantityResponseDto;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderQuantityResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.multitenancy.multitenancy.util.TenantContext;
import com.groyyo.order.management.dto.request.PurchaseOrderQuantityCreateDto;
import com.groyyo.order.management.service.PurchaseOrderQuantityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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

    @GetMapping("get/colour/{purchaseOrderId}/lineType/{lineType}")
    public ResponseDto<List<ColourQuantityResponseDto>> getColoursByPoID(@PathVariable String purchaseOrderId,@PathVariable LineType lineType) {
        log.info("Request received to get all colours and its quantity by PurchaseOrderId :{}", purchaseOrderId);
        String factoryId = TenantContext.getTenantId();
        List<ColourQuantityResponseDto> response = purchaseOrderQuantityService.getColoursByPoID(purchaseOrderId, factoryId,lineType);
        return ResponseDto.success(" Pending colours " + response.size() + " quantity available to assign checkers in the system", response);
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
