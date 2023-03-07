package com.groyyo.order.management.dto.request;

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

    private List<PurchaseOrderQuantityRequestDto> purchaseOrderQuantityRequest;

}