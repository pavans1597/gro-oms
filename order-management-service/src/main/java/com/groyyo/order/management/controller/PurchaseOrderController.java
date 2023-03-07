package com.groyyo.order.management.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groyyo.core.base.common.dto.PageResponse;
import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.response.PurchaseOrderResponseDto;
import com.groyyo.order.management.service.PurchaseOrderQuantityService;
import com.groyyo.order.management.service.PurchaseOrderService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("purchase/order")
public class PurchaseOrderController {

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private PurchaseOrderQuantityService purchaseOrderQuantityService;

	@GetMapping("/get/all")
	public ResponseDto<List<PurchaseOrderResponseDto>> getAllPurchaseOrders(@RequestParam(value = "status", required = false) Boolean status) {

		log.info("Request received to get all purchaseOrders");

		List<PurchaseOrderResponseDto> purchaseOrderResponseDtos = purchaseOrderService.getAllPurchaseOrders(status);

		return ResponseDto.success("Found " + purchaseOrderResponseDtos.size() + " purchaseOrders in the system", purchaseOrderResponseDtos);
	}

	@GetMapping("/get/{id}")
	public ResponseDto<PurchaseOrderResponseDto> getPurchaseOrder(@PathVariable String id) {

		log.info("Request received to get purchaseOrder with id: {}", id);

		PurchaseOrderResponseDto purchaseOrderResponseDto = purchaseOrderService.getPurchaseOrderById(id);

		return Objects.isNull(purchaseOrderResponseDto) ? ResponseDto.failure("Found no purchaseOrder with id: " + id)
				: ResponseDto.success("Found purchaseOrder with id: " + id, purchaseOrderResponseDto);
	}

	@GetMapping("get/listing/{page}/{limit}")
	public ResponseDto<PageResponse<PurchaseOrderResponseDto>> getPurchaseOrderListing(@PathVariable(name = "page", value = "page") int page,
			@PathVariable(name = "limit", value = "limit") int limit) {

		log.info("Request received to get listing of purchase orders with page: {} and limit: {}", page, limit);

		PageResponse<PurchaseOrderResponseDto> purchaseOrderResponseDtos = purchaseOrderService.getPurchaseOrderListing(page, limit);

		return ResponseDto.success("Page Response", purchaseOrderResponseDtos);
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
