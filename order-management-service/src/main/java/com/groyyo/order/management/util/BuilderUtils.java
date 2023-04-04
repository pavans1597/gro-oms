package com.groyyo.order.management.util;

import java.util.ArrayList;
import java.util.List;

import com.groyyo.order.management.adapter.PartAdapter;
import com.groyyo.order.management.adapter.PurchaseOrderAdapter;
import com.groyyo.order.management.dto.request.BulkColorRequestDto;
import com.groyyo.order.management.dto.request.BulkOrderExcelRequestDto;
import com.groyyo.order.management.dto.request.BulkPartRequestDto;
import com.groyyo.order.management.dto.request.BulkPurchaseOrderRequestDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BuilderUtils {

	private static ArrayList<BulkColorRequestDto> getColors(BulkOrderExcelRequestDto bulkOrderExcelData) {
		return new ArrayList<>(
				List.of(new BulkColorRequestDto(bulkOrderExcelData.getColor(), bulkOrderExcelData.getSizes())));
	}

	public BulkPurchaseOrderRequestDto buildBulkPOFromExcel(BulkOrderExcelRequestDto bulkOrderExcelData) {
		ArrayList<BulkColorRequestDto> colors = getColors(bulkOrderExcelData);
		BulkPartRequestDto part = PartAdapter.buildPartFromBulkOrderRequest(bulkOrderExcelData, colors);
		return PurchaseOrderAdapter.buildBulkPOFromExcel(bulkOrderExcelData, part);
	}

}
