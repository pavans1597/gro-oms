package com.groyyo.order.management.service;

import java.util.List;
import java.util.Map;

import com.groyyo.core.master.dto.request.ColorRequestDto;
import com.groyyo.core.master.dto.response.ColorResponseDto;
import com.groyyo.order.management.entity.Color;

public interface ColorService {

	List<ColorResponseDto> getAllColors(Boolean status);

	ColorResponseDto getColorById(String id);

	ColorResponseDto addColor(ColorRequestDto colorRequestDto);

	ColorResponseDto updateColor(ColorRequestDto colorRequestDto);

	ColorResponseDto activateDeactivateColor(String id, boolean status);

	void consumeColor(ColorResponseDto colorResponseDto);

	Color findOrCreate(String name, String hexCode);

	/**
	 * @param colorRequestDto
	 * @return
	 */
	ColorResponseDto conditionalSaveColor(ColorResponseDto colorResponseDto);

	/**
	 * @param colorByNameMap
	 */
	void saveEntityFromCache(Map<String, ColorResponseDto> colorByNameMap);
}
