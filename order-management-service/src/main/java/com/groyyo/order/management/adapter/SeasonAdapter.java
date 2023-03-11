
package com.groyyo.order.management.adapter;

import com.groyyo.core.master.dto.request.SeasonRequestDto;
import com.groyyo.core.master.dto.response.SeasonResponseDto;
import com.groyyo.order.management.entity.Season;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SeasonAdapter {

	public Season buildSeasonFromRequest(SeasonRequestDto seasonRequestDto) {

		return Season
				.builder()
				.name(seasonRequestDto.getName())
				.masterId(seasonRequestDto.getMasterId())
				.build();
	}

	public Season buildSeasonFromResponse(SeasonResponseDto seasonResponseDto) {

		return Season
				.builder()
				.name(seasonResponseDto.getName())
				.masterId(seasonResponseDto.getMasterId())
				.build();
	}

	public Season cloneSeasonWithRequest(SeasonRequestDto seasonRequestDto, Season season) {

		if (StringUtils.isNotBlank(seasonRequestDto.getName()))
			season.setName(seasonRequestDto.getName());

		if (StringUtils.isNotBlank(seasonRequestDto.getMasterId()))
			season.setMasterId(seasonRequestDto.getMasterId());

		return season;
	}

	public SeasonRequestDto buildRequestFromResponse(SeasonResponseDto seasonResponseDto) {

		SeasonRequestDto seasonRequestDto = SeasonRequestDto.builder().build();

		if (StringUtils.isNotBlank(seasonResponseDto.getName()))
			seasonRequestDto.setName(seasonResponseDto.getName());

		if (StringUtils.isNotBlank(seasonResponseDto.getMasterId()))
			seasonRequestDto.setMasterId(seasonResponseDto.getMasterId());

		return seasonRequestDto;
	}

	public SeasonResponseDto buildResponseFromEntity(Season season) {

		return SeasonResponseDto
				.builder()
				.uuid(season.getUuid())
				.name(season.getName())
				.masterId(season.getMasterId())
				.status(season.isStatus())
				.factoryId(season.getFactoryId())
				.build();
	}

	public List<SeasonResponseDto> buildResponsesFromEntities(List<Season> seasons) {

		return seasons.stream().map(SeasonAdapter::buildResponseFromEntity).collect(Collectors.toList());
	}

}
