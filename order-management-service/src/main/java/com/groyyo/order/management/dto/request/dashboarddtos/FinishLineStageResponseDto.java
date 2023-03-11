package com.groyyo.order.management.dto.request.dashboarddtos;

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
public class FinishLineStageResponseDto extends BaseRequestDto {

    AlterationCountResponseDto alterationCountResponse;
    CheckersCountResponseDto checkersCountResponseDto;
    OrdersCountResponseDto ordersCountResponseDto;
    QualityCountResponseDto qualityCountResponseDto;

}
