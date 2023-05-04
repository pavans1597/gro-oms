/**
 * 
 */
package com.groyyo.order.management.http.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.groyyo.core.multitenancy.multitenancy.util.TenantContext;
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
	public ResponseDto<FactoryResponseDto> getFactory() {
		ResponseDto<FactoryResponseDto> factory = null;

		try {
			factory = factoryClientApi.getFactory(TenantContext.getTenantId());
		} catch (Exception e) {
			log.error("Exception caught while getting factories data from user-service ", e);
		}

		return factory;
	}

	@Override
	public String getFactoryId() {
		return getFactory().getId();
	}

}
