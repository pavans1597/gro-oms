package com.groyyo.order.dto.response;

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
public class FabricResponseDto extends BaseResponseDto {

    private String fabricCategory;
    private String imageUrl;
    private String fabricCode;

}
