/**
 * 
 */
package com.groyyo.order.management.controller.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groyyo.core.base.common.dto.ResponseDto;
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
}
