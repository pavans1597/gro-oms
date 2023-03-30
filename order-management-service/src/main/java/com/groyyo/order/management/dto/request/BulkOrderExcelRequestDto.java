package com.groyyo.order.management.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Map;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BulkOrderExcelRequestDto {


    private String purchaseOrderNumber;

    private String styleNumber;

    private String styleName;

    private String productName;

    private String buyerName;

    private String seasonName;

    private String fitName;

    private String exFtyDate;

    private String part;

    private Double variance;

    private String color;

    private String sizeGroup;

    private Map<String, Long> sizes;

}
