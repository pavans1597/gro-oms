package com.groyyo.order.management.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.order.management.dto.response.LineResponseDto;
import com.groyyo.order.management.dto.response.PurchaseOrderAndLineColourResponse;
import com.groyyo.core.multitenancy.multitenancy.util.TenantContext;
import com.groyyo.order.management.http.service.UserManagementHttpService;
import com.groyyo.order.management.service.LineCheckerAssignmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@RestController
@RequestMapping("line")
@RequiredArgsConstructor
@Log4j2
public class LineController {

	private final UserManagementHttpService userManagementHttpService;

	@Autowired
	private LineCheckerAssignmentService lineCheckerAssignmentService;

    @GetMapping("fetch/all")
    public ResponseDto<Map<LineType, List<LineResponseDto>>>  getAllLines() {
        String factoryId = TenantContext.getTenantId();

		log.info("Request received to get getAll Lines by FactoryId : {} ", factoryId);

        ResponseDto<Map<LineType, List<LineResponseDto>>>  listResponseDto = userManagementHttpService.getAllLines(factoryId);

		return (Objects.isNull(listResponseDto) || listResponseDto.getData().isEmpty()) ? ResponseDto.failure(" Lines not found ")
				: ResponseDto.success(" Lines retrieved successfully ", listResponseDto.getData());

	}

	@GetMapping("fetch/type")
	public ResponseDto<List<LineResponseDto>> getLinesByType(@RequestParam("lineType") LineType lineType) {
		String factoryId = TenantContext.getTenantId();

		log.info("Request received to get getAll Lines by FactoryId : {} ", factoryId);

		ResponseDto<List<LineResponseDto>> listResponseDto = userManagementHttpService.getLinesByType(factoryId, lineType);

		return (Objects.isNull(listResponseDto) || listResponseDto.getData().isEmpty()) ? ResponseDto.failure(" Lines not found ")
				: ResponseDto.success(" Lines retrieved successfully ", listResponseDto.getData());

	}

	@GetMapping("fetch/lines/colour")
	public ResponseDto<PurchaseOrderAndLineColourResponse> getLinesAndColour(@RequestParam("purchaseOrderId") String purchaseOrderId) {

		log.info("Request received to get getAll Lines and it colours assigned to purchaseOrderId : {} ", purchaseOrderId);

		PurchaseOrderAndLineColourResponse responses = lineCheckerAssignmentService.getLinesAndColourByPoId(purchaseOrderId);

		return Objects.nonNull(responses) ? ResponseDto.success(("Lines and colours assigned to purchase order Id : "), responses)
				: ResponseDto.failure("No lines and colours are assigned for po");
	}
}
