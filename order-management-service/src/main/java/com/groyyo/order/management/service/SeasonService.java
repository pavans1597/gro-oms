package com.groyyo.order.management.service;

import java.util.List;
import java.util.Map;

import com.groyyo.core.master.dto.request.SeasonRequestDto;
import com.groyyo.core.master.dto.response.SeasonResponseDto;
import com.groyyo.order.management.entity.Season;

/**
 * @author nipunaggarwal
 *
 */
public interface SeasonService {

	/**
	 * @param status
	 * @return
	 */
	List<SeasonResponseDto> getAllSeasons(Boolean status);

	/**
	 * @param id
	 * @return
	 */
	SeasonResponseDto getSeasonById(String id);

	/**
	 * @param seasonRequestDto
	 * @return
	 */
	SeasonResponseDto addSeason(SeasonRequestDto seasonRequestDto);

	/**
	 * @param seasonRequestDto
	 * @return
	 */
	SeasonResponseDto updateSeason(SeasonRequestDto seasonRequestDto);

	/**
	 * @param id
	 * @param status
	 * @return
	 */
	SeasonResponseDto activateDeactivateSeason(String id, boolean status);

	/**
	 * @param seasonResponseDto
	 */
	void consumeSeason(SeasonResponseDto seasonResponseDto);

	/**
	 * @param seasonByNameMap
	 */
	void saveEntityFromCache(Map<String, SeasonResponseDto> seasonByNameMap);

	Season findOrCreate(String name);

	/**
	 * @param seasonResponseDto
	 * @return
	 */
	SeasonResponseDto conditionalSaveSeason(SeasonResponseDto seasonResponseDto);
}
