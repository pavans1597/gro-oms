package com.groyyo.order.management.dto.request;

import com.groyyo.core.master.dto.request.BaseRequestDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderRequestDto {

    private String id;

    private String name;

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

    private String orderId;

    private String orderName;

    private List<PurchaseOrderQuantityRequestDto> purchaseOrderQuantityRequest;

    private StyleRequestDto styleRequestDto;

}
