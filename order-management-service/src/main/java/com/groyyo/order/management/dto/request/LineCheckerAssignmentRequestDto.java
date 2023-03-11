package com.groyyo.order.management.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class LineCheckerAssignmentRequestDto {

    private List<UserLineDetails> assignment;
    @JsonProperty("purchaseOrderId")
    @NotNull
    private String purchaseOrderId;
    private String salesOrderId;

}

