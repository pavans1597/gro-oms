package com.groyyo.order.management.dto.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.master.dto.response.BaseResponseDto;
import com.groyyo.order.management.dto.request.UserLineDetails;
import com.groyyo.order.management.enums.PurchaseOrderStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class PurchaseOrderResponseDto extends BaseResponseDto {
	private String purchaseOrderNumber;

	private String styleId;

	private String styleNumber;

	private String styleName;

	private String productId;

	private String productName;

	private String fabricId;

	private String fabricName;

	private String buyerId;

	private String buyerName;

	private Double tolerance;

	private Date receiveDate;

	private Date exFtyDate;

	private String seasonId;

	private String fitId;

	private String partId;

	private Long totalQuantity;

	private Long totalTargetQuantity;

	@Builder.Default
	private PurchaseOrderStatus purchaseOrderStatus = PurchaseOrderStatus.DRAFT;

	@Builder.Default
	private List<UserLineDetails> userLineDetails = new ArrayList<UserLineDetails>();

	@Builder.Default
	private Map<LineType, List<UserLineDetails>> userLineDetailsMap = new HashMap<LineType, List<UserLineDetails>>();
}
