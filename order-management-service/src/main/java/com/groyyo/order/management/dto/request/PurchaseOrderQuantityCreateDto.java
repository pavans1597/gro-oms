package com.groyyo.order.management.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderQuantityCreateDto {

    private String id;
    private String name;
    private String purchaseOrderId;
    private String sizeId;
    private Long quantity;
    private String colourId;
    private Long targetQuantity;

}
