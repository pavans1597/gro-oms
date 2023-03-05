package com.groyyo.order.management.dto.response;

import com.groyyo.core.master.dto.response.BaseResponseDto;
import com.groyyo.order.management.dto.request.PurchaseOrderQuantityRequestDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class PurchaseOrderResponseDto extends BaseResponseDto {
    private String name;

    private String styleId;

    private String styleNumber;

    private String styleName;

    private String productId;

    private String productName;

    private String fabricId;

    private String fabricName;

    private String buyerId;

    private String buyerName;

    private Double tolerance;

    private Date receiveDate;

    private Date exFtyDate;

    private String seasonId;

    private String fitId;

    private String partId;

    private String orderId;

    private String orderName;
    @Builder.Default
    private List<PurchaseOrderQuantityRequestDto> sizeIds = new ArrayList<>();
}
