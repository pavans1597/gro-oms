package com.groyyo.order.management.dto.response;

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
public class PurchaseOrderQuantityResponseDto{

    private String id;

    private String name;

    private String purchaseOrderId;

    private String sizeId;

    private String colourId;

    private Long quantity;

    private Long targetQuantity;

}
