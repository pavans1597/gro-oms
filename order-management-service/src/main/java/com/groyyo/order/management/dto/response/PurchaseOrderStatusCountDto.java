/**
 * 
 */
package com.groyyo.order.management.dto.response;

import java.util.HashMap;
import java.util.Map;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author nipunaggarwal
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class PurchaseOrderStatusCountDto {

	@Builder.Default
	private Map<PurchaseOrderStatus, Long> purchaseOrderStatusCount = new HashMap<PurchaseOrderStatus, Long>();

}
