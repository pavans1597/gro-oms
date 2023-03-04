package com.groyyo.order.management.dto.request;

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
public class StyleRequestDto extends BaseRequestDto {

    private String styleNumber;

    private String image;

    private String cadImage;

    private String productId;
}
