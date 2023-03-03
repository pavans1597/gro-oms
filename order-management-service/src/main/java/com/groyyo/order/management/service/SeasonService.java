package com.groyyo.order.management.service;

import java.util.List;

import com.groyyo.core.master.dto.request.SeasonRequestDto;
import com.groyyo.core.master.dto.response.SeasonResponseDto;

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
}

