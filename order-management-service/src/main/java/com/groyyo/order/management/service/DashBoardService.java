package com.groyyo.order.management.service;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.order.management.dto.request.dashboarddtos.DashBoardLineStageResponseDto;

public interface DashBoardService {




    DashBoardLineStageResponseDto getLineStageDetailsByLineType(String factoryId, LineType lineType);
}
