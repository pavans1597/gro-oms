package com.groyyo.order.management.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BulkPurchaseOrderRequestDto {
    private String name;

    private String styleNumber;

    private String styleName;

    private String productName;

    private String buyerName;

    private String seasonName;

    private String fitName;

    private Date exFtyDate;

    private BulkPartRequestDto part;

}