package com.groyyo.order.management.controller;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
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
@RequestMapping("user")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserManagementHttpService userManagementHttpService;


    @GetMapping("fetch")
    public ResponseDto<List<UserResponseDto>> getUsers(@RequestParam LineType lineType) {
        String factoryId = HeaderUtil.getFactoryIdHeaderValue();

        log.info("Request received to get getUsers by FactoryId : {}", factoryId);

        ResponseDto<List<UserResponseDto>> lineUsers = userManagementHttpService.getUsersByLineType(factoryId, lineType);

        return (Objects.isNull(lineUsers) || lineUsers.getData().isEmpty()) ? ResponseDto.failure(" Users not found ") : ResponseDto.success(" Users retrieved successfully  ", lineUsers.getData());
    }
}
