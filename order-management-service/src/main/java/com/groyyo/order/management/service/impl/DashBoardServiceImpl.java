package com.groyyo.order.management.service.impl;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.order.management.dto.request.dashboarddtos.*;
import com.groyyo.order.management.service.DashBoardService;
import com.groyyo.order.management.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	public DashBoardLineStageResponseDto getProductionLineStageDetails(String factoryId, LineType linesType) {
		OrdersCountResponseDto ordersDetailsCounts = getOrdersDetailsCounts(factoryId, linesType);
		QualityCountResponseDto qualityCheckCounts = getQualityCheckCounts(factoryId, linesType);
		CheckersCountResponseDto checkersDetailsCounts = getCheckersDetailsCounts(factoryId, linesType);
		AlterationCountResponseDto inAlterationCounts = getInAlterationCounts(factoryId,linesType);
		return buildProductLineStageResponseDto(ordersDetailsCounts, qualityCheckCounts, checkersDetailsCounts, inAlterationCounts);
	}

	public DashBoardLineStageResponseDto getFinishLineStageDetails(String factoryId, LineType linesType) {
		OrdersCountResponseDto ordersDetailsCounts = getOrdersDetailsCounts(factoryId, linesType);
		QualityCountResponseDto qualityCheckCounts = getQualityCheckCounts(factoryId, linesType);
		CheckersCountResponseDto checkersDetailsCounts = getCheckersDetailsCounts(factoryId, linesType);
		AlterationCountResponseDto inAlterationCounts = getInAlterationCounts(factoryId, linesType);
		return buildDashBoardLineStageResponseDto(ordersDetailsCounts, qualityCheckCounts, checkersDetailsCounts, inAlterationCounts);
	}

	@Override
	public DashBoardLineStageResponseDto getLineStageDetailsByLineType(String factoryId, LineType lineType) {
		switch (lineType){
			case PRODUCTION_LINE:
				DashBoardLineStageResponseDto finishLineStageDetails = getFinishLineStageDetails(factoryId, lineType);
				return finishLineStageDetails ;
			case FINISH_LINE:
				DashBoardLineStageResponseDto productionLineStageDetails = getProductionLineStageDetails(factoryId, lineType);
				return  productionLineStageDetails ;
			default :
				return null;
		}
	}

	private DashBoardLineStageResponseDto buildProductLineStageResponseDto(OrdersCountResponseDto ordersDetailsCounts, QualityCountResponseDto qualityCheckCounts,
																		   CheckersCountResponseDto checkersDetailsCounts, AlterationCountResponseDto inAlterationCounts) {
		return DashBoardLineStageResponseDto.builder()
				.qualityCountResponseDto(qualityCheckCounts)
				.checkersCountResponseDto(checkersDetailsCounts)
				.alterationCountResponse(inAlterationCounts)
				.ordersCountResponseDto(ordersDetailsCounts)
				.build();
	}

	private DashBoardLineStageResponseDto buildDashBoardLineStageResponseDto(OrdersCountResponseDto ordersDetailsCounts, QualityCountResponseDto qualityCheckCounts,
			CheckersCountResponseDto checkersDetailsCounts, AlterationCountResponseDto inAlterationCounts) {
		return DashBoardLineStageResponseDto.builder()
				.qualityCountResponseDto(qualityCheckCounts)
				.checkersCountResponseDto(checkersDetailsCounts)
				.alterationCountResponse(inAlterationCounts)
				.ordersCountResponseDto(ordersDetailsCounts)
				.build();
	}

	private AlterationCountResponseDto getInAlterationCounts(String factoryId, LineType linesType) {
		// quality check call
		return purchaseOrderService.getAlterationsCounts(factoryId, linesType);
	}

	private CheckersCountResponseDto getCheckersDetailsCounts(String factoryId, LineType linesType) {
		// order service call
		return purchaseOrderService.getCheckersDetailsCounts(factoryId, linesType);

	}

	private QualityCountResponseDto getQualityCheckCounts(String factoryId, LineType linesType) {
		// quality check call
		return purchaseOrderService.getQualityCheckDetailsCounts(factoryId, linesType);
	}

	private OrdersCountResponseDto getOrdersDetailsCounts(String factoryId, LineType linesType) {

		return purchaseOrderService.getOrdersDetailsCounts(factoryId, linesType);

	}
}
