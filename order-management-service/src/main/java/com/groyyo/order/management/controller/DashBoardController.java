package com.groyyo.order.management.controller;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.order.management.dto.request.dashboarddtos.DashBoardLineStageResponseDto;
import com.groyyo.order.management.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dashboard")
public class DashBoardController {

	@Autowired
	DashBoardService dashboardService;

	@GetMapping("show/productionLineStage/numbers")
	public ResponseDto<DashBoardLineStageResponseDto> geLineStageDetails(@RequestParam("lineType")LineType lineType) {
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();
		DashBoardLineStageResponseDto productionLineStageDetails = dashboardService.getLineStageDetailsByLineType(factoryId,lineType);
		return ResponseDto.success(productionLineStageDetails);

	}


}
