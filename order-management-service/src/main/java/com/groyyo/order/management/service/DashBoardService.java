package com.groyyo.order.management.service;

import com.groyyo.order.management.dto.request.dashboarddtos.FinishLineStageResponseDto;
import com.groyyo.order.management.dto.request.dashboarddtos.ProductionLineStageResponseDto;

public interface DashBoardService {


    ProductionLineStageResponseDto getProductionLineStageDetails(String factoryId);

    FinishLineStageResponseDto getFinishLineStageDetails(String factoryId);
}
