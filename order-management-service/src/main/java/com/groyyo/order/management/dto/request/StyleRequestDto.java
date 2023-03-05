package com.groyyo.order.management.dto.request;

import com.groyyo.core.master.dto.request.BaseRequestDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StyleRequestDto{

    private String id;
    private String name;

    private String styleNumber;

    private String image;

    private String cadImage;

    private String productId;
}
