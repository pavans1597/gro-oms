/**
 * 
 */
package com.groyyo.order.management.controller.internal;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.order.management.dto.request.PurchaseOrderListRequestDto;
import com.groyyo.order.management.dto.response.PurchaseOrderDetailResponseDto;
import com.groyyo.order.management.dto.response.PurchaseOrderStatusCountDto;
import com.groyyo.order.management.service.PurchaseOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * @author nipunaggarwal
 *
 */
@Log4j2
@RestController
@RequestMapping("internal/purchase/order")
public class InternalPurchaseOrderController {

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@PutMapping("mark/complete/id/{purchaseOrderId}")
	public ResponseDto<Void> markPurchaseOrderCompleteAndRemoveAssignments(@PathVariable String purchaseOrderId) {

		log.info("Request received to mark complete purchase order with uuid: {}", purchaseOrderId);

		purchaseOrderService.markPurchaseOrderCompleteAndRemoveAssignments(purchaseOrderId);

		return ResponseDto.success("Marked purchase order completed with id: " + purchaseOrderId);
	}

	@GetMapping("get/status/counts")
	public ResponseDto<PurchaseOrderStatusCountDto> getAllPurchaseOrderStatusCounts(@RequestParam(value = "status", required = false) Boolean status) {

		log.info("Request received to get purchase order status counts with status: {}", status);

		PurchaseOrderStatusCountDto purchaseOrderStatusCounts = purchaseOrderService.getPurchaseOrderStatusCounts(status);

		return ResponseDto.success("Found purchaseOrder status counts in the system", purchaseOrderStatusCounts);
	}

	@GetMapping("get/status/counts/date-wise")
	public ResponseDto<PurchaseOrderStatusCountDto> getAllPurchaseOrderStatusCounts(@RequestParam(value = "status", required = false) Boolean status,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(value = "poStatus", required = false) PurchaseOrderStatus purchaseOrderStatus) {

		log.info("Request received to get purchase order status counts with status: {} and startDate: {} and endDate: {}", status, startDate, endDate);

		PurchaseOrderStatusCountDto purchaseOrderStatusCounts = purchaseOrderService.getPurchaseOrderStatusCounts(status, startDate, endDate,purchaseOrderStatus);

		return ResponseDto.success("Found purchaseOrder status counts in the system", purchaseOrderStatusCounts);
	}

	@PostMapping("publish")
	public ResponseDto<Void> publishPurchaseOrderPackets(@RequestBody @Valid PurchaseOrderListRequestDto purchaseOrderListRequestDto) {

		log.info("Request received to publish purchase order ids: {}", purchaseOrderListRequestDto.getPurchaseOrderIds());

		purchaseOrderService.publishPurchaseOrderPackets(purchaseOrderListRequestDto.getPurchaseOrderIds());

		return ResponseDto.success("Published purchase order ids: " + purchaseOrderListRequestDto.getPurchaseOrderIds());
	}

	@PostMapping("get/purchaseOrders")
	public ResponseDto<List<PurchaseOrderDetailResponseDto>> getPurchaseOrdersStatusWise(@RequestBody List<PurchaseOrderStatus> requestDto) {
		log.info("Request received to publish Purchase orders based on status", requestDto);
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();
		List<PurchaseOrderDetailResponseDto> purchaseOrderStatusWiseResponse = purchaseOrderService.getPurchaseOrdersStatusWise(requestDto, factoryId);
		return ResponseDto.success("PurchaseOrders retrieved successfully " + purchaseOrderStatusWiseResponse.size(), purchaseOrderStatusWiseResponse);
	}
}
