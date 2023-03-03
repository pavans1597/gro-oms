package com.groyyo.order.management.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAssinmentDto {
    @JsonProperty("assigned_by")
    private String assignedBy;

    @JsonProperty("purchase_order_id")
    private String purchaseOrderId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("size_id")
    private String sizeId;

    @JsonProperty("quantity")
    private double quantity;

    @JsonProperty("fabric_id")
    private String fabricId;

    @JsonProperty("color_id")
    private String colourId;
    @JsonProperty("qc_stage")
    private String qcStage;

    @JsonProperty("message_id")
    private String messageId;
    @JsonProperty("status")
    private boolean status;
    @JsonProperty("updated_by")
    private String upDatedBy;
    @JsonProperty("updated_at")
    private Date upDatedAt;
    @JsonProperty("task_id")
    private long taskId;

}
