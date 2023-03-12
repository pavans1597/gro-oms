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
public class PurchaseOrderQuantityRequestDto {

	private String sizeId;

	private String sizeName;

	private Long quantity;

	private String colourId;

	private String colorName;

}
