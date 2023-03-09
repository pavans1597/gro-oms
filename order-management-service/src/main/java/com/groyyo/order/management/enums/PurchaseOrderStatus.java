/**
 * 
 */
package com.groyyo.order.management.enums;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author nipunaggarwal
 *
 */
@Getter
@AllArgsConstructor
public enum PurchaseOrderStatus {

	DRAFT(0, "In_Draft"),
	YET_TO_START(1, "Created"),
	ONGOING(2, "In_Progress"),
	COMPLETED(3, "Completed");

	private final int sequenceId;
	private final String statusName;

	public static Set<PurchaseOrderStatus> getAllPurchaseOrderStatuses() {

		return Set.of(PurchaseOrderStatus.values());
	}

	public static Set<String> getAllPurchaseOrderStatusNames() {

		return getAllPurchaseOrderStatuses().stream().map(PurchaseOrderStatus::getStatusName).collect(Collectors.toSet());
	}
}
