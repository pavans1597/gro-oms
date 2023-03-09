package com.groyyo.order.management.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class LineAssignmentRequestDto {

    private List<LineAssignment> assignment;
    private String purchaseOrderId;
    private String salesOrderId;

}

