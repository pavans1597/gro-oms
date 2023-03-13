package com.groyyo.order.management.adapter;

import com.groyyo.order.management.dto.request.dashboarddtos.OrdersCountResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DashBoardAnalyticsAdapter {


    public OrdersCountResponseDto buildOrderCountResponseByCounts(long yetToStartCount, long completedCount, long onGoing, long totalCount) {

        return OrdersCountResponseDto.builder()
                .totalOrders(totalCount)
                .inProgress(onGoing)
                .yetToStart(yetToStartCount)
                .completed(completedCount)
                .build();

    }
}
