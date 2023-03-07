package com.groyyo.order.management.controller;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.response.PurchaseOrderResponseDto;
import com.groyyo.order.management.service.PurchaseOrderQuantityService;
import com.groyyo.order.management.service.PurchaseOrderService;
import com.groyyo.order.management.service.StyleService;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Log4j2
@RestController
@RequestMapping("purchase/order")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseOrderQuantityService purchaseOrderQuantityService;

    @Autowired
    private StyleService styleService;

    @GetMapping("/get/all")
    public ResponseDto<List<PurchaseOrderResponseDto>> getAllPurchaseOrders(
            @RequestParam(value = "status", required = false) Boolean status,
            @RequestParam(value = "pageSize", required = false) Short pageSize,
            @RequestParam(value = "pageIndex", required = false) Short pageIndex,
            @RequestParam(value = "startDate", required = false) DateTime startDate,
            @RequestParam(value = "endDate", required = false) DateTime endDate,
            @RequestParam(value = "searchText", required = false) String searchText,
            @RequestParam(value = "searchType", required = false) String searchType
    ) {

        log.info("Request received to get all purchaseOrders");

        List<PurchaseOrderResponseDto> purchaseOrderResponseDtos = purchaseOrderService.getAllPurchaseOrders(status);

        return ResponseDto.success("Found " + purchaseOrderResponseDtos.hashCode() + " purchaseOrders in the system", purchaseOrderResponseDtos);
    }

    @GetMapping("/get/{id}")
    public ResponseDto<PurchaseOrderResponseDto> getPurchaseOrder(@PathVariable String id) {

        log.info("Request received to get purchaseOrder with id: {}", id);

        PurchaseOrderResponseDto purchaseOrderResponseDto = purchaseOrderService.getPurchaseOrderById(id);

        return Objects.isNull(purchaseOrderResponseDto) ? ResponseDto.failure("Found no purchaseOrder with id: " + id)
                : ResponseDto.success("Found purchaseOrder with id: " + id, purchaseOrderResponseDto);
    }

    @PostMapping("/add")
    public ResponseDto<PurchaseOrderResponseDto> addPurchaseOrder(@RequestBody @Valid PurchaseOrderRequestDto purchaseOrderRequestDto) {

        log.info("Request received to add purchaseOrder: {}", purchaseOrderRequestDto);
//        StyleResponseDto styleResponse = styleService.addStyle(purchaseOrderRequestDto.getStyleRequestDto());
        PurchaseOrderResponseDto purchaseOrderResponse = purchaseOrderService.addPurchaseOrder(purchaseOrderRequestDto);
        purchaseOrderQuantityService.addBulkPurchaseOrderQuantity(purchaseOrderRequestDto.getPurchaseOrderQuantityRequest(), purchaseOrderResponse.getUuid(), purchaseOrderRequestDto.getTolerance());

        return ResponseDto.success("PurchaseOrder added successfully !!", purchaseOrderResponse);
    }

    @PostMapping("/update")
    public ResponseDto<PurchaseOrderResponseDto> updatePurchaseOrder(@RequestBody PurchaseOrderUpdateDto purchaseOrderUpdateDto) {

        log.info("Request received to update purchaseOrder: {}", purchaseOrderUpdateDto);

        PurchaseOrderResponseDto purchaseOrderResponseDto = purchaseOrderService.updatePurchaseOrder(purchaseOrderUpdateDto);

        return Objects.isNull(purchaseOrderResponseDto) ? ResponseDto.failure("Unable to update purchaseOrder !!")
                : ResponseDto.success("PurchaseOrder updated successfully !!", purchaseOrderResponseDto);
    }
}
