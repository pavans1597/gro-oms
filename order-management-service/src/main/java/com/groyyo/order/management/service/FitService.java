package com.groyyo.order.management.service;

import java.util.List;
import java.util.Map;

import com.groyyo.core.master.dto.request.FitRequestDto;
import com.groyyo.core.master.dto.response.FitResponseDto;
import com.groyyo.order.management.entity.Fit;

public interface FitService {

	/**
	 * @param status
	 * @return
	 */
	List<FitResponseDto> getAllFits(Boolean status);

	/**
	 * @param id
	 * @return
	 */
	FitResponseDto getFitById(String id);

	/**
	 * @param fitRequestDto
	 * @return
	 */
	FitResponseDto addFit(FitRequestDto fitRequestDto);

	/**
	 * @param fitRequestDto
	 * @return
	 */
	FitResponseDto updateFit(FitRequestDto fitRequestDto);

	/**
	 * @param id
	 * @param status
	 * @return
	 */
	FitResponseDto activateDeactivateFit(String id, boolean status);

	/**
	 * @param fitResponseDto
	 */
	void consumeFit(FitResponseDto fitResponseDto);

	/**
	 * @param fitByNameMap
	 */
	void saveEntityFromCache(Map<String, FitResponseDto> fitByNameMap);

	Fit findOrCreate(String name);

	/**
	 * @param fitResponseDto
	 * @return
	 */
	FitResponseDto conditionalSaveFit(FitResponseDto fitResponseDto);
}
