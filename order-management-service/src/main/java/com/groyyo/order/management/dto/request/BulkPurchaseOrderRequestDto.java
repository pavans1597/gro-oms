package com.groyyo.order.management.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    private String seasonName;

    private String fitName;

    private Date exFtyDate;

    private BulkPartRequestDto part;

}