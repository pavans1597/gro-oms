/**
 * 
 */
package com.groyyo.order.management.dto.filter;

import java.util.Date;
import java.util.List;

import com.groyyo.core.dto.BaseFilterDto;

import lombok.AllArgsConstructor;
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
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderFilterDto extends BaseFilterDto {

	private String purchaseOrderNumber;
	private String fabricName;
	private String buyerName;
	private Date receiveDate;
	private Date exFtyDate;
	private String fitId;
	private String partId;
	private String styleName;
	private String styleNumber;
	private String productId;
	private String productName;
	private String colorId;
	private String colorName;
	private Long quantity;
	private Long targetQuantity;
	private List<String> uuids;
}
