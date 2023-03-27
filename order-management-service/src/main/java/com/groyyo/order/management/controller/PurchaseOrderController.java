package com.groyyo.order.management.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groyyo.core.base.common.dto.PageResponse;
import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderResponseDto;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.order.management.dto.filter.PurchaseOrderFilterDto;
import com.groyyo.order.management.dto.request.*;
import com.groyyo.order.management.dto.response.PurchaseOrderStatusCountDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.service.LineCheckerAssignmentService;
import com.groyyo.order.management.service.PurchaseOrderService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("purchase/order")
public class PurchaseOrderController {

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private LineCheckerAssignmentService lineCheckerAssignmentService;

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

	@PostMapping("get/listing/{page}/{limit}")
	public ResponseDto<PageResponse<PurchaseOrderResponseDto>> getPurchaseOrderListing(@RequestBody PurchaseOrderFilterDto purchaseOrderFilterDto,
			@RequestParam(name = "poStatus", value = "poStatus") PurchaseOrderStatus purchaseOrderStatus,
			@PathVariable(name = "page", value = "page") int page,
			@PathVariable(name = "limit", value = "limit") int limit) {

		log.info("Request received to get listing of purchase orders with purchaseOrderFilterDto: {}, purchaseOrderStatus: {}, page: {}, limit: {}", purchaseOrderFilterDto, purchaseOrderStatus, page,
				limit);

		PageResponse<PurchaseOrderResponseDto> purchaseOrderResponseDtos = purchaseOrderService.getPurchaseOrderListing(purchaseOrderFilterDto, purchaseOrderStatus, page, limit);

		return ResponseDto.success("Page Response for Purchase Orders", purchaseOrderResponseDtos);
	}

	@GetMapping("get/status/counts")
	public ResponseDto<PurchaseOrderStatusCountDto> getAllPurchaseOrderStatusCounts(@RequestParam(value = "status", required = false) Boolean status) {

		log.info("Request received to get purchase order status counts with status: {}", status);

		PurchaseOrderStatusCountDto purchaseOrderStatusCounts = purchaseOrderService.getPurchaseOrderStatusCounts(status);

		return ResponseDto.success("Found purchaseOrder status counts in the system", purchaseOrderStatusCounts);
	}

	@PostMapping("add")
	public ResponseDto<PurchaseOrderResponseDto> addPurchaseOrder(@RequestBody @Valid PurchaseOrderRequestDto purchaseOrderRequestDto) {

		log.info("Request received to add purchaseOrder: {}", purchaseOrderRequestDto);

		PurchaseOrderResponseDto purchaseOrderResponseDto = purchaseOrderService.addPurchaseOrder(purchaseOrderRequestDto);

		return ResponseDto.success("PurchaseOrder added successfully !!", purchaseOrderResponseDto);
	}

	@PostMapping("update")
	public ResponseDto<PurchaseOrderResponseDto> updatePurchaseOrder(@RequestBody PurchaseOrderUpdateDto purchaseOrderUpdateDto) {

		log.info("Request received to update purchaseOrder: {}", purchaseOrderUpdateDto);

		PurchaseOrderResponseDto purchaseOrderResponseDto = purchaseOrderService.updatePurchaseOrder(purchaseOrderUpdateDto);

		return Objects.isNull(purchaseOrderResponseDto) ? ResponseDto.failure("Unable to update purchaseOrder !!")
				: ResponseDto.success("PurchaseOrder updated successfully !!", purchaseOrderResponseDto);
	}

	@PostMapping("assign/checkers")
	public ResponseDto<List<LineCheckerAssignment>> assignCheckers(@RequestBody LineCheckerAssignmentRequestDto checkerAssignDto) {

		log.info("Request received to update assign Checkers: {}", checkerAssignDto);

		String factoryIdHeaderValue = HeaderUtil.getFactoryIdHeaderValue();

		List<LineCheckerAssignment> lineCheckerAssignments = lineCheckerAssignmentService.lineCheckerAssignment(checkerAssignDto, factoryIdHeaderValue);

		return Objects.isNull(lineCheckerAssignments) ? ResponseDto.failure("Unable to assign Checkers to purchaseOrder !!")
				: ResponseDto.success(" Checkers Assigned Successfully !!", lineCheckerAssignments);
	}

	@PutMapping("id/{id}/status/{purchaseOrderStatus}/change")
	public ResponseDto<Void> changePurchaseOrderStatus(@PathVariable String id, @PathVariable PurchaseOrderStatus purchaseOrderStatus, @RequestParam boolean forceUpdate) {

		log.info("Request received to update status of purchase order with uuid: {} to: {} with force update flag value: {} ", id, purchaseOrderStatus, forceUpdate);

		purchaseOrderService.changeStatusOfPurchaseOrder(id, purchaseOrderStatus, forceUpdate);

		return ResponseDto.success("Updated status of purchase order with id: " + id);
	}

	@PutMapping("mark/complete/id/{purchaseOrderId}")
	public ResponseDto<Void> markPurchaseOrderCompleteAndRemoveAssignments(@PathVariable String purchaseOrderId) {

		log.info("Request received to mark complete purchase order with uuid: {}", purchaseOrderId);

		purchaseOrderService.markPurchaseOrderCompleteAndRemoveAssignments(purchaseOrderId);

		return ResponseDto.success("Marked purchase order completed with id: " + purchaseOrderId);
	}

	@PostMapping("bulk/add")
	public ResponseDto<List<PurchaseOrderResponseDto>> addBulkPurchaseOrder(@RequestBody @Valid List<BulkOrderExcelRequestDto> bulkOrderExcelRequestsDto) {

		log.info("Request received to add purchaseOrder: {}", bulkOrderExcelRequestsDto);

		List<PurchaseOrderResponseDto> purchaseOrderResponses = purchaseOrderService.createBulkOrderFromExcel(bulkOrderExcelRequestsDto);

		return Objects.isNull(purchaseOrderResponses) ? ResponseDto.failure("Unable to add purchase orders")
				: ResponseDto.success("PurchaseOrders added successfully !!", purchaseOrderResponses);
	}
}
