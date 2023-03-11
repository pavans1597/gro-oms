package com.groyyo.order.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.order.management.dto.request.dashboarddtos.FinishLineStageResponseDto;
import com.groyyo.order.management.dto.request.dashboarddtos.ProductionLineStageResponseDto;
import com.groyyo.order.management.service.DashBoardService;

@RestController
@RequestMapping("dashboard")
public class DashBoardController {

	@Autowired
	DashBoardService dashboardService;

	@GetMapping("show/productionLineStage/numbers")
	public ResponseDto<ProductionLineStageResponseDto> getProductionLineStageDetails() {

		ProductionLineStageResponseDto productionLineStageDetails = dashboardService.getProductionLineStageDetails(null);
		return ResponseDto.success(productionLineStageDetails);

	}

	@GetMapping("show/finishLineStage/numbers")
	public ResponseDto<FinishLineStageResponseDto> getfinishLineStageDetails() {

		FinishLineStageResponseDto finishLineStageDetails = dashboardService.getFinishLineStageDetails("factoryId");
		return ResponseDto.success(finishLineStageDetails);

	}

}
