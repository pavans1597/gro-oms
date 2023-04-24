package com.groyyo.order.management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BulkPurchaseOrderRequestDto {
	private String name;

	private String styleNumber;

	private String styleName;

	private String productName;

	private String buyerName;

	private String seasonName;

	private String fitName;

	private String exFtyDate;

	private BulkPartRequestDto part;

}