/**
 * 
 */
package com.groyyo.order.management.http.service;

import java.util.List;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.user.dto.response.FactoryResponseDto;

/**
 * @author nipunaggarwal
 *
 */
public interface FactoryHttpService {

	/**
	 * @return
	 */
	ResponseDto<List<FactoryResponseDto>> getAllFactories();

	/**
	 * @return
	 */
	List<String> getFactoryIds();

}
