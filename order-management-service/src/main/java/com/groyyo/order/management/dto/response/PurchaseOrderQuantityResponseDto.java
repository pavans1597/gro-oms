package com.groyyo.order.management.dto.response;

import lombok.*;
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
    private String factoryId;

}
