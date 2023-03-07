package com.groyyo.order.management.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class PurchaseOrderUpdateDto extends PurchaseOrderRequestDto{
private String id;

}
