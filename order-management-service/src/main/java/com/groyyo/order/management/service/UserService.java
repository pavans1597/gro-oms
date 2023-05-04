package com.groyyo.order.management.service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.core.enums.QcUserType;
import com.groyyo.order.management.dto.request.CheckersCountResponseDto;

import java.util.List;

public interface UserService {
     ResponseDto<List<UserResponseDto>> getUsersByLineType(String orgId, String factoryId, LineType lineType, QcUserType qcUserType);

     CheckersCountResponseDto getUsersCountByLineType(String orgId, String factoryId, LineType lineType, QcUserType qcUserType);
}
