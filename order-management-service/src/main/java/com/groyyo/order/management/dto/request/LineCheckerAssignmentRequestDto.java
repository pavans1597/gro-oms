package com.groyyo.order.management.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groyyo.core.dto.PurchaseOrder.UserLineDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class LineCheckerAssignmentRequestDto {

	private List<UserLineDetails> assignment;

	@JsonProperty("purchaseOrderId")
	@NotNull
	private String purchaseOrderId;

	private String salesOrderId;

}
