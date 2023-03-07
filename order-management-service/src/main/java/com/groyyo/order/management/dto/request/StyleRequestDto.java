package com.groyyo.order.management.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StyleRequestDto {
    private String name;

    private String styleNumber;

    private String styleImageId;

    private String cadImageId;

    private String productId;

    private String productName;
}
