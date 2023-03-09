/**
 * 
 */
package com.groyyo.order.management.constants;

import lombok.experimental.UtilityClass;

/**
 * @author nipunaggarwal
 *
 */
@UtilityClass
public class FilterConstants {

	/*
	 * Generic Filters (usually present in abstract JPA entity)
	 */
	public final String ID = "id";
	public final String UUID = "uuid";
	public final String NAME = "name";
	public final String STATUS = "status";
	public final String CREATED_BY = "createdBy";
	public final String CREATED_AT = "createdAt";
	public final String UPDATED_BY = "updatedBy";
	public final String UPDATED_AT = "updatedAt";

	@UtilityClass
	public class PurchaseOrderFilterConstants {

		/*
		 * Purchase Order Filters
		 */
		public final String PURCHASE_ORDER_STATUS = "purchaseOrderStatus";
		public final String PURCHASE_ORDER_NUMBER = "name";
		public final String PURCHASE_ORDER_FABRIC_NAME = "fabricName";
		public final String PURCHASE_ORDER_BUYER_NAME = "buyerName";
		public final String PURCHASE_ORDER_STYLE_NUMBER = "styleNumber";
		public final String PURCHASE_ORDER_STYLE_NAME = "styleName";
		public final String PURCHASE_ORDER_PRODUCT_NAME = "productName";
		public final String PURCHASE_ORDER_EX_FTY_DATE = "exFtyDate";
		public final String PURCHASE_ORDER_RECEIVE_DATE = "receiveDate";
	}

}
