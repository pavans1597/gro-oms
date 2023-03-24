package com.groyyo.order.management.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.util.Date;
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

    private Date exFtyDate;

    private String part;

    private Double variance;

    private String color;

    private String sizeGroup;

    private Map<String, Long> sizes;

}
