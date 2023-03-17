/**
 *
 */
package com.groyyo.order.management.http.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineResponseDto;
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.core.enums.QcUserType;
import com.groyyo.core.user.client.api.UserClientApi;
import com.groyyo.order.management.http.service.UserManagementHttpService;

import lombok.extern.log4j.Log4j2;

/**
 * @author pavan
 *
 */
@Log4j2
@Service
public class UserManagementHttpServiceImpl implements UserManagementHttpService {

	@Autowired
	private UserClientApi userClientApi;

	@Override
	public ResponseDto<List<UserResponseDto>> getUsersByLineType(String factoryId, LineType lineType, QcUserType qcUserType) {

		try {

			return userClientApi.getUsers(factoryId, lineType, qcUserType);

		} catch (Exception e) {

			log.error("Exception occured while calling getUsersByLineType service ", e);

		}

		return null;

	}

	@Override
	public int getUserCountByTypeAndLine(String qcUserType, String lineType) {
		return 0;
	}

	@Override
	public ResponseDto<List<LineResponseDto>> getLinesByType(String factoryId, LineType lineType) {
		try {
			return userClientApi.getLinesByType(factoryId, lineType);
		} catch (Exception e) {
			log.error("exception occured while calling getLineUsers service ");
		}
		return null;
	}

	@Override
	public ResponseDto<List<LineResponseDto>> getAllLines(String factoryId) {
		try {
			return userClientApi.getLines(factoryId);
		} catch (Exception e) {
			log.error("exception occured while calling getAllLines service ");
		}
		return null;
	}

	@Override
	public ResponseDto<List<UserResponseDto>> getUsersByDepartmentAndRole(String factoryId, String departmentName, QcUserType qcUserType) {
		try {

			return userClientApi.getUsersByRoleAndDept(factoryId, departmentName, qcUserType);

		} catch (Exception e) {

			log.error("exception occured while calling getUsersByLineType service ");

		}

		return null;
	}

}