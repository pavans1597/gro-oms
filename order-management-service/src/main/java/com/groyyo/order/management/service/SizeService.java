package com.groyyo.order.management.service;

import java.util.List;
import java.util.Map;

import com.groyyo.core.master.dto.request.SizeRequestDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;
import com.groyyo.order.management.entity.Size;

public interface SizeService {

	List<SizeResponseDto> getAllSizes(Boolean status);

	SizeResponseDto getSizeById(String id);

	SizeResponseDto addSize(SizeRequestDto sizeRequestDto);

	SizeResponseDto updateSize(SizeRequestDto sizeRequestDto);

	SizeResponseDto activateDeactivateSize(String id, boolean status);

	void consumeSize(SizeResponseDto sizeResponseDto);

	void saveEntityFromCache(Map<String, SizeResponseDto> sizeByNameMap);

	Size findOrCreate(String name);

	/**
	 * @param sizeResponseDto
	 * @return
	 */
	SizeResponseDto conditionalSaveSize(SizeResponseDto sizeResponseDto);
}
