package com.groyyo.order.management.service.dto.response;

import com.groyyo.core.master.dto.response.BaseResponseDto;
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
public class FabricCategoryResponseDto extends BaseResponseDto {

    private String type;

}