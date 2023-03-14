package com.groyyo.order.management.controller;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.core.enums.QcUserType;
import com.groyyo.order.management.dto.request.CheckersCountResponseDto;
import com.groyyo.order.management.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;


    @GetMapping("{qcUserType}/fetch")
    public ResponseDto<List<UserResponseDto>> getUsers(@PathVariable("qcUserType") QcUserType qcUserType , @RequestParam LineType lineType) {
        String factoryId = HeaderUtil.getFactoryIdHeaderValue();

        log.info("Request received to get getUsers by FactoryId : {}", factoryId);

        ResponseDto<List<UserResponseDto>> lineUsers = userService.getUsersByLineType(factoryId, lineType,qcUserType);

        return (Objects.isNull(lineUsers) || lineUsers.getData().isEmpty()) ? ResponseDto.failure(" Users not found ") : ResponseDto.success(" Users retrieved successfully  ", lineUsers.getData());
    }


    @GetMapping("{qcUserType}/count")
    public ResponseDto<CheckersCountResponseDto> getUsersCount(@PathVariable("qcUserType") QcUserType qcUserType , @RequestParam LineType lineType) {
        String factoryId = HeaderUtil.getFactoryIdHeaderValue();

        log.info("Request received to get getUsers by FactoryId : {}", factoryId);

        CheckersCountResponseDto lineUsers = userService.getUsersCountByLineType(factoryId, lineType,qcUserType);

        return (Objects.isNull(lineUsers)) ? ResponseDto.failure(" Users not found ") : ResponseDto.success(" Users count successfully  ", lineUsers);
    }


}
