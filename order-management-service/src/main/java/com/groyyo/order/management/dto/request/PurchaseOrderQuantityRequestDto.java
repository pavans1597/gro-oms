package com.groyyo.order.management.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderQuantityRequestDto {
    private String sizeId;
    private Long quantity;
    private String colourId;

}
