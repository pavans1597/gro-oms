package com.groyyo.order.management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.groyyo.core.dto.userservice.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineUserResponseDto extends BaseResponseDto {
    String id;
    String name;
    boolean status;
}
