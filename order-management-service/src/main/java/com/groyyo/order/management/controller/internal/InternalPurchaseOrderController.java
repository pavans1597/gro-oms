/**
 * 
 */
package com.groyyo.order.management.controller.internal;

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

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.order.management.dto.request.PurchaseOrderListRequestDto;
import com.groyyo.order.management.dto.response.PurchaseOrderStatusCountDto;
import com.groyyo.order.management.service.PurchaseOrderService;

import lombok.extern.log4j.Log4j2;

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

	@PostMapping("publish")
	public ResponseDto<Void> publishPurchaseOrderPackets(@RequestBody @Valid PurchaseOrderListRequestDto purchaseOrderListRequestDto) {

		log.info("Request received to publish purchase order ids: {}", purchaseOrderListRequestDto.getPurchaseOrderIds());

		purchaseOrderService.publishPurchaseOrderPackets(purchaseOrderListRequestDto.getPurchaseOrderIds());

		return ResponseDto.success("Published purchase order ids: " + purchaseOrderListRequestDto.getPurchaseOrderIds());
	}
}
