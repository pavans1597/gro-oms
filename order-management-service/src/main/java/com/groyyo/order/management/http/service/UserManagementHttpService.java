package com.groyyo.order.management.http.service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.core.enums.QcUserType;

import java.util.List;

public interface UserManagementHttpService {


    ResponseDto<List<UserResponseDto>> getUsersByLineType(String factoryId, LineType lineType, QcUserType qcUserType);

    int getUserCountByTypeAndLine(String qcUserType, String lineType);

    ResponseDto<List<LineResponseDto>> getLinesByType(String factoryIdHeaderValue, LineType lineType);

    ResponseDto<List<LineResponseDto>> getAllLines(String factoryId);


    ResponseDto<List<UserResponseDto>> getUsersByDepartmentAndRole(String factoryId, String departmentName, QcUserType qcUserType);
}
