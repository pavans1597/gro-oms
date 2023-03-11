package com.groyyo.order.management.adapter;

import com.groyyo.order.management.dto.request.dashboarddtos.OrdersCountResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DashBoardAnalyticsAdapter {


    public OrdersCountResponseDto buildOrderCountResponseByCounts(Long yetToStartCount, Long completedCount, Long onGoing, Long totalCount) {

        return OrdersCountResponseDto.builder()
                .totalOrders(totalCount)
                .inProgress(onGoing)
                .yetToStart(yetToStartCount)
                .completed(completedCount)
                .build();

    }
}
