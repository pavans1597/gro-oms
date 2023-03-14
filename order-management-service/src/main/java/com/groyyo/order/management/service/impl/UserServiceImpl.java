package com.groyyo.order.management.service.impl;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.core.enums.QcUserType;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.dto.request.CheckersCountResponseDto;
import com.groyyo.order.management.http.service.UserManagementHttpService;
import com.groyyo.order.management.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class UserServiceImpl implements UserService {


    @Autowired
    private UserManagementHttpService userManagementHttpService;
    @Autowired
    private LineCheckerAssignmentDbService lineCheckerAssignmentDbService;
    @Override
    public ResponseDto<List<UserResponseDto>> getUsersByLineType(String factoryId, LineType lineType, QcUserType qcUserType) {
        try {
            return userManagementHttpService.getUsersByLineType(factoryId, lineType, qcUserType);
        } catch (Exception e) {
            log.error("error occured : ", e);
        }
        return  null;
    }

    @Override
    public CheckersCountResponseDto getUsersCountByLineType(String factoryId, LineType lineType, QcUserType qcUserType) {

        //Re Factory call a single user api to get users by department
        long currentUsersCount = userManagementHttpService.getUsersByLineType(factoryId, lineType,qcUserType).getData().size();

        long assignedUserCount = lineCheckerAssignmentDbService.countLineCheckerByFactoryId(factoryId,lineType, true);

        long availableUsersCount = Math.max(0, (currentUsersCount - assignedUserCount));
        assignedUserCount = Math.min(assignedUserCount, currentUsersCount);

        return CheckersCountResponseDto.builder()
                .assigned(assignedUserCount)
                .totalChecker(currentUsersCount)
                .available(availableUsersCount)
                .build();
    }



}
