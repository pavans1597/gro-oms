package com.groyyo.order.management.dto.response;

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
public class StyleResponseDto extends BaseResponseDto {

    private String Id;
    private String styleNumber;

    private String image;

    private String cadImage;

    private String productId;
}
