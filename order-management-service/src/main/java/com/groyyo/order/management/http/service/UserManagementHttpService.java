package com.groyyo.order.management.http.service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.enums.QcUserType;
import com.groyyo.order.management.dto.response.UserResponseDto;

import java.util.List;
import java.util.Map;

public interface UserManagementHttpService {


    ResponseDto<List<UserResponseDto>> getUsersByLineType(String orgId, String factoryId, LineType lineType, QcUserType qcUserType);

    int getUserCountByTypeAndLine(String qcUserType, String lineType);

    ResponseDto<List<LineResponseDto>> getLinesByType(String factoryIdHeaderValue, LineType lineType);

    ResponseDto<Map<LineType, List<LineResponseDto>>> getAllLines(String factoryId);


    ResponseDto<List<UserResponseDto>> getUsersByDepartmentAndRole(String orgId, String factoryId, String departmentName, QcUserType qcUserType);
}
