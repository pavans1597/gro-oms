package com.groyyo.order.management.service.impl;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.order.management.dto.request.dashboarddtos.*;
import com.groyyo.order.management.service.DashBoardService;
import com.groyyo.order.management.service.PurchaseOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class DashBoardServiceImpl implements DashBoardService {

    @Autowired
    PurchaseOrderService purchaseOrderService ;

    @Override
    public ProductionLineStageResponseDto getProductionLineStageDetails(String factoryId) {
        OrdersCountResponseDto ordersDetailsCounts = getOrdersDetailsCounts(factoryId, LineType.PRODUCTION_LINE);
        QualityCountResponseDto qualityCheckCounts = getQualityCheckCounts(factoryId, LineType.PRODUCTION_LINE);
        CheckersCountResponseDto checkersDetailsCounts = getCheckersDetailsCounts(factoryId, LineType.PRODUCTION_LINE);
        AlterationCountResponseDto inAlterationCounts = getInAlterationCounts(factoryId, LineType.PRODUCTION_LINE);
        return buildProductLineStageResponseDto(ordersDetailsCounts, qualityCheckCounts, checkersDetailsCounts, inAlterationCounts);
    }

    @Override
    public FinishLineStageResponseDto getFinishLineStageDetails(String factoryId) {
        OrdersCountResponseDto ordersDetailsCounts = getOrdersDetailsCounts(factoryId, LineType.FINISH_LINE);
        QualityCountResponseDto qualityCheckCounts = getQualityCheckCounts(factoryId, LineType.FINISH_LINE);
        CheckersCountResponseDto checkersDetailsCounts = getCheckersDetailsCounts(factoryId, LineType.FINISH_LINE);
        AlterationCountResponseDto inAlterationCounts = getInAlterationCounts(factoryId, LineType.FINISH_LINE);
        return buildFinishLineStageResponseDto(ordersDetailsCounts, qualityCheckCounts, checkersDetailsCounts, inAlterationCounts);
    }

    private  ProductionLineStageResponseDto buildProductLineStageResponseDto(OrdersCountResponseDto ordersDetailsCounts, QualityCountResponseDto qualityCheckCounts, CheckersCountResponseDto checkersDetailsCounts, AlterationCountResponseDto inAlterationCounts) {
        return ProductionLineStageResponseDto.builder()
                .qualityCountResponseDto(qualityCheckCounts)
                .checkersCountResponseDto(checkersDetailsCounts)
                .alterationCountResponse(inAlterationCounts)
                .ordersCountResponseDto(ordersDetailsCounts)
                .build();
    }

    private  FinishLineStageResponseDto buildFinishLineStageResponseDto(OrdersCountResponseDto ordersDetailsCounts, QualityCountResponseDto qualityCheckCounts, CheckersCountResponseDto checkersDetailsCounts, AlterationCountResponseDto inAlterationCounts) {
        return FinishLineStageResponseDto.builder()
                .qualityCountResponseDto(qualityCheckCounts)
                .checkersCountResponseDto(checkersDetailsCounts)
                .alterationCountResponse(inAlterationCounts)
                .ordersCountResponseDto(ordersDetailsCounts)
                .build();
    }
    private AlterationCountResponseDto getInAlterationCounts(String factoryId, LineType linesType) {
        //quality check call
        return null;
    }

    private CheckersCountResponseDto getCheckersDetailsCounts(String factoryId, LineType linesType) {
        //order service call
        return purchaseOrderService.getCheckersDetailsCounts(factoryId,linesType);

    }

    private QualityCountResponseDto getQualityCheckCounts(String factoryId, LineType linesType) {
        //quality check call

        return null;

    }

    private OrdersCountResponseDto getOrdersDetailsCounts(String factoryId, LineType linesType) {

        return purchaseOrderService.getOrdersDetailsCounts(factoryId,linesType);

    }
}
