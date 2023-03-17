/**
 * 
 */
package com.groyyo.order.management.http.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.user.client.api.FactoryClientApi;
import com.groyyo.core.user.dto.response.FactoryResponseDto;
import com.groyyo.order.management.http.service.FactoryHttpService;

import lombok.extern.log4j.Log4j2;

/**
 * @author nipunaggarwal
 *
 */
@Log4j2
@Service
public class FactoryHttpServiceImpl implements FactoryHttpService {

	@Autowired
	private FactoryClientApi factoryClientApi;

	@Override
	public ResponseDto<List<FactoryResponseDto>> getAllFactories() {

		ResponseDto<List<FactoryResponseDto>> factories = null;

		try {

			factories = factoryClientApi.getAllFactories();

		} catch (Exception e) {

			log.error("Exception caught while getting factories data from user-service ", e);
		}

		return factories;
	}

	@Override
	public List<String> getFactoryIds() {

		List<String> factoryIds = new ArrayList<String>();

		ResponseDto<List<FactoryResponseDto>> factoryResponseDto = getAllFactories();

		if (Objects.nonNull(factoryResponseDto) && factoryResponseDto.isStatus()) {

			List<FactoryResponseDto> factoryResponseDtos = factoryResponseDto.getData();

			factoryIds = factoryResponseDtos.stream().map(FactoryResponseDto::getId).collect(Collectors.toList());
		}

		return factoryIds;
	}

}
