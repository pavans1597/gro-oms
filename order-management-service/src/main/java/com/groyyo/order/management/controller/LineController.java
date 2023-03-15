package com.groyyo.order.management.controller;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.userservice.LineResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.order.management.http.service.UserManagementHttpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("line")
@RequiredArgsConstructor
@Log4j2
public class LineController {

    private final UserManagementHttpService userManagementHttpService;


    @GetMapping("fetch/all")
    public ResponseDto<List<LineResponseDto>> getAllLines() {
        String factoryId = HeaderUtil.getFactoryIdHeaderValue();

        log.info("Request received to get getAll Lines by FactoryId : {} ", factoryId);

        ResponseDto<List<LineResponseDto>> listResponseDto = userManagementHttpService.getAllLines(factoryId);

        return (Objects.isNull(listResponseDto) || listResponseDto.getData().isEmpty()) ? ResponseDto.failure(" Lines not found ")
                : ResponseDto.success(" Lines retrieved successfully ", listResponseDto.getData());

    }


    @GetMapping("fetch/type")
    public ResponseDto<List<LineResponseDto>> getLinesByType(@RequestParam("lineType")LineType lineType) {
        String factoryId = HeaderUtil.getFactoryIdHeaderValue();

        log.info("Request received to get getAll Lines by FactoryId : {} ", factoryId);

        ResponseDto<List<LineResponseDto>> listResponseDto = userManagementHttpService.getLinesByType(factoryId,lineType);

        return (Objects.isNull(listResponseDto) || listResponseDto.getData().isEmpty()) ? ResponseDto.failure(" Lines not found ")
                : ResponseDto.success(" Lines retrieved successfully ", listResponseDto.getData());

    }
}
