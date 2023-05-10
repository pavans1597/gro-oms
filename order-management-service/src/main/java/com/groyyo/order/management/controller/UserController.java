package com.groyyo.order.management.controller;

import com.groyyo.core.base.common.dto.GroyyoUser;
import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.core.enums.QcUserType;
import com.groyyo.core.multitenancy.multitenancy.domain.entity.Tenant;
import com.groyyo.core.multitenancy.multitenancy.util.TenantContext;
import com.groyyo.order.management.dto.request.CheckersCountResponseDto;
import com.groyyo.order.management.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
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
    public ResponseDto<List<UserResponseDto>> getUsers(Authentication authentication,
                                                       @PathVariable("qcUserType") QcUserType qcUserType ,
                                                       @RequestParam LineType lineType) {
        String factoryId = HeaderUtil.getFactoryIdHeaderValue();

        log.info("Request received to get getUsers by FactoryId : {}", factoryId);

        GroyyoUser userDetails = (GroyyoUser) authentication.getPrincipal();
        ResponseDto<List<UserResponseDto>> lineUsers = userService.getUsersByLineType(userDetails.getOrgId(), factoryId, lineType,qcUserType);

        return (Objects.isNull(lineUsers) || lineUsers.getData().isEmpty()) ? ResponseDto.failure(" Users not found ") : ResponseDto.success(" Users retrieved successfully  ", lineUsers.getData());
    }


    @GetMapping("{qcUserType}/count")
    public ResponseDto<CheckersCountResponseDto> getUsersCount(Authentication authentication,
                                                               @PathVariable("qcUserType") QcUserType qcUserType ,
                                                               @RequestParam LineType lineType) {
        String factoryId = TenantContext.getTenantId();

        log.info("Request received to get getUsers by FactoryId : {}", factoryId);

        GroyyoUser userDetails = (GroyyoUser) authentication.getPrincipal();

        CheckersCountResponseDto lineUsers = userService.getUsersCountByLineType(userDetails.getOrgId(), factoryId, lineType,qcUserType);

        return (Objects.isNull(lineUsers)) ? ResponseDto.failure(" Users not found ") : ResponseDto.success(" Users count successfully  ", lineUsers);
    }
}
