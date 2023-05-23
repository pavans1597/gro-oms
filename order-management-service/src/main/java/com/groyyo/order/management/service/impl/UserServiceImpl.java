package com.groyyo.order.management.service.impl;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.enums.QcUserType;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.dto.request.CheckersCountResponseDto;
import com.groyyo.order.management.dto.response.UserResponseDto;
import com.groyyo.order.management.http.service.UserManagementHttpService;
import com.groyyo.order.management.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    private static final String finishLineDepartment=  "FINISHING" ;
    private static final String productionLineDepartment=  "PRODUCTION" ;
    @Autowired
    private UserManagementHttpService userManagementHttpService;
    @Autowired
    private LineCheckerAssignmentDbService lineCheckerAssignmentDbService;
    @Override
    public ResponseDto<List<UserResponseDto>> getUsersByLineType(String orgId, String factoryId, LineType lineType, QcUserType qcUserType) {
        try {
            String departmentName = null ;
            switch (lineType){
                case FINISH_LINE:
                    departmentName = finishLineDepartment ;
                    break;
                case PRODUCTION_LINE:
                    departmentName = productionLineDepartment ;
                    break;
            }
            return userManagementHttpService.getUsersByDepartmentAndRole(orgId, factoryId, departmentName, qcUserType);
        } catch (Exception e) {
            log.error("error occured : ", e);
        }
        return  null;
    }

    @Override
    public CheckersCountResponseDto getUsersCountByLineType(String orgId, String factoryId, LineType lineType, QcUserType qcUserType) {

        //Re Factory call a single user api to get users by department
        long currentUsersCount = userManagementHttpService.getUsersByLineType(orgId, factoryId, lineType,qcUserType).getData().size();

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
