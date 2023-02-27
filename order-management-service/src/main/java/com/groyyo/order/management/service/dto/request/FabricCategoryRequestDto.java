package com.groyyo.order.management.service.dto.request;

import com.groyyo.core.master.dto.request.BaseRequestDto;
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
public class FabricCategoryRequestDto extends BaseRequestDto {

    private String type;

}