/**
 *
 */
package com.groyyo.order.management.http.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.groyyo.order.management.dto.response.LineResponseDto;
import com.groyyo.order.management.dto.response.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.userservice.LineType;
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
	public ResponseDto<List<UserResponseDto>> getUsersByLineType(String orgId, String factoryId, LineType lineType, QcUserType qcUserType) {
		try {
			ResponseDto<List<com.groyyo.core.dto.userservice.UserResponseDto>> response = userClientApi.getUsers(orgId, factoryId, lineType, qcUserType);

			if(response != null) {
				List<com.groyyo.core.dto.userservice.UserResponseDto> users = response.getData();

				return ResponseDto.success( users.stream().map(userOfLine ->
					UserResponseDto.builder()
							.lastName(userOfLine.getFirstName())
							.emailId(userOfLine.getEmail())
							.phone(userOfLine.getPhone())
							.factoryId(factoryId)
							.userType(userOfLine.getUserType())
							.build()

				).collect(Collectors.toList()));
			}
		} catch (Exception e) {
			log.error("Exception occured while calling getUsersByLineType service ", e);
		}

		return ResponseDto.success(Collections.emptyList());
	}

	@Override
	public int getUserCountByTypeAndLine(String qcUserType, String lineType) {
		return 0;
	}

	@Override
	public ResponseDto<List<LineResponseDto>> getLinesByType(String factoryId, LineType lineType) {
		try {
			ResponseDto<List<com.groyyo.core.dto.userservice.LineResponseDto>> response =
					userClientApi.getLinesByType(factoryId, lineType);

			if(response != null) {
				List<com.groyyo.core.dto.userservice.LineResponseDto> lines = response.getData();

				return ResponseDto.success(
						lines.stream().map(line ->
										LineResponseDto.builder()
												.lineTypeName(line.getLineName())
												.lineType(line.getLineType())
												.factoryId(factoryId)
												.status(line.isStatus())
												.build()
								).collect(Collectors.toList())
				);
			}

		} catch (Exception e) {
			log.error("exception occured while calling getLineUsers service ");
		}

		return ResponseDto.success(Collections.emptyList());
	}

	@Override
	public ResponseDto<Map<LineType, List<LineResponseDto>>> getAllLines(String factoryId) {
		try {
			ResponseDto<Map<LineType, List<com.groyyo.core.dto.userservice.LineResponseDto>>> response =
					userClientApi.getLines(factoryId);

			if(response != null) {
				Map<LineType, List<com.groyyo.core.dto.userservice.LineResponseDto>> lineMap = response.getData();

				Map<LineType, List<LineResponseDto>> lines = new HashMap<>();

				lineMap.forEach((lineType, lineList) -> {
					List<LineResponseDto> lineData = lineList.stream().map(line ->
						LineResponseDto.builder()
								.lineTypeName(line.getLineName())
								.lineType(line.getLineType())
								.factoryId(factoryId)
								.status(line.isStatus())
								.build()
					).collect(Collectors.toList());

					lines.put(lineType, lineData);
				});

				return ResponseDto.success(lines);
			}
		} catch (Exception e) {
			log.error("exception occured while calling getAllLines service ");
		}

		return ResponseDto.success(Collections.emptyMap());
	}

	@Override
	public ResponseDto<List<UserResponseDto>> getUsersByDepartmentAndRole(String orgId, String factoryId, String departmentName, QcUserType qcUserType) {
		try {
			ResponseDto<List<com.groyyo.core.dto.userservice.UserResponseDto>> response =  userClientApi.getUsersByRoleAndDept(orgId, factoryId, departmentName, qcUserType);

			if(response != null) {
				List<com.groyyo.core.dto.userservice.UserResponseDto> users = response.getData();

				return ResponseDto.success( users.stream().map(userOfLine ->
						UserResponseDto.builder()
								.lastName(userOfLine.getFirstName())
								.emailId(userOfLine.getEmail())
								.phone(userOfLine.getPhone())
								.factoryId(factoryId)
								.userType(userOfLine.getUserType())
								.build()

				).collect(Collectors.toList()));
			}
		} catch (Exception e) {
			log.error("exception occurred while calling getUsersByLineType service ");
		}

		return ResponseDto.success(Collections.emptyList());
	}
}