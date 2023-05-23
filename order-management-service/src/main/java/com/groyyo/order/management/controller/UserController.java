package com.groyyo.order.management.controller;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.enums.QcUserType;
import com.groyyo.core.multitenancy.multitenancy.util.TenantContext;
import com.groyyo.order.management.dto.request.CheckersCountResponseDto;
import com.groyyo.order.management.dto.response.LineUserResponseDto;
import com.groyyo.order.management.dto.response.UserResponseDto;
import com.groyyo.order.management.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;


    @GetMapping("{qcUserType}/fetch")
    public ResponseDto<List<LineUserResponseDto>> getUsers(JwtAuthenticationToken authentication,
                                                       @PathVariable("qcUserType") QcUserType qcUserType ,
                                                       @RequestParam LineType lineType) {
        String factoryId = TenantContext.getTenantId();

        log.info("Request received to get getUsers by FactoryId : {}", factoryId);

        String orgId = (String) authentication.getTokenAttributes().get("orgId");
        ResponseDto<List<LineUserResponseDto>> lineUsers = ResponseDto.success(userService.getUsersByLineType(orgId, factoryId, lineType,qcUserType)
                .getData().stream().map(lineUser -> LineUserResponseDto.builder()
                        .id(lineUser.getId())
                        .name(lineUser.getLastName())
                        .status(lineUser.isStatus())
                        .build()).collect(Collectors.toList()));

        return lineUsers.getData().isEmpty()
                ? ResponseDto.failure(" Users not found ")
                : ResponseDto.success(" Users retrieved successfully  ", lineUsers.getData());
    }


    @GetMapping("{qcUserType}/count")
    public ResponseDto<CheckersCountResponseDto> getUsersCount(JwtAuthenticationToken authentication,
                                                               @PathVariable("qcUserType") QcUserType qcUserType ,
                                                               @RequestParam LineType lineType) {
        String factoryId = TenantContext.getTenantId();

        log.info("Request received to get getUsers by FactoryId : {}", factoryId);

        String orgId = (String) authentication.getTokenAttributes().get("orgId");

        CheckersCountResponseDto lineUsers = userService.getUsersCountByLineType(orgId, factoryId, lineType,qcUserType);

        return (Objects.isNull(lineUsers)) ? ResponseDto.failure(" Users not found ") : ResponseDto.success(" Users count successfully  ", lineUsers);
    }
}
