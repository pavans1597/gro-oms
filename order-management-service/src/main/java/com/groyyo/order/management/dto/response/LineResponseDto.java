package com.groyyo.order.management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.groyyo.core.dto.userservice.BaseResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineResponseDto extends BaseResponseDto {
    private String id;
    private String factoryId;
    private LineType lineType;
    private boolean status;
    private String lineTypeName;
    private String name;
}
