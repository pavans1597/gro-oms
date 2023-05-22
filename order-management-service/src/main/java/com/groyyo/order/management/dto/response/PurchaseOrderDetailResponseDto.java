package com.groyyo.order.management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseOrderDetailResponseDto {

    private String poId;
    private String poName;
    private PurchaseOrderStatus purchaseOrderStatus;
    private Date exFtyDate;
    private boolean assignWithColours;

}
