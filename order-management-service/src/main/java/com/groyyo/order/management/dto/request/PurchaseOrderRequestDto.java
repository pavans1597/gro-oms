package com.groyyo.order.management.dto.request;

import java.util.Date;
import java.util.List;

import com.groyyo.core.dto.PurchaseOrder.StyleDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class PurchaseOrderRequestDto {

	private String purchaseOrderNumber;

	private String fabricId;

	private String fabricName;

	private String buyerId;

	private String buyerName;

	@Builder.Default
	private Double tolerance = 0.0d;

	private Date receiveDate;

	private Date exFtyDate;

	private String seasonId;

	private String seasonName;

	private String fitId;

	private String fitName;

	private String partId;

	private String partName;

	private List<PurchaseOrderQuantityRequestDto> purchaseOrderQuantityRequest;

	private StyleDto styleRequestDto;

}
