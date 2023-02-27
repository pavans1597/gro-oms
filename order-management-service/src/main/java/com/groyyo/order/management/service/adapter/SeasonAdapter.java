
package com.groyyo.order.management.service.adapter;

import com.groyyo.core.master.dto.request.SeasonRequestDto;
import com.groyyo.core.master.dto.response.SeasonResponseDto;
import com.groyyo.order.management.service.entity.Season;
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
                .build();
    }

    public Season buildSeasonFromResponse(SeasonResponseDto seasonResponseDto) {

        return Season
                .builder()
                .name(seasonResponseDto.getName())
                .build();
    }

    public Season cloneSeasonWithRequest(SeasonRequestDto seasonRequestDto, Season season) {

        if (StringUtils.isNotBlank(seasonRequestDto.getName()))
            season.setName(seasonRequestDto.getName());

        return season;
    }

    public SeasonResponseDto buildResponseFromEntity(Season season) {

        return SeasonResponseDto
                .builder()
                .uuid(season.getUuid())
                .name(season.getName())
                .status(season.isStatus())
                .build();
    }

    public List<SeasonResponseDto> buildResponsesFromEntities(List<Season> seasons) {

        return seasons.stream().map(SeasonAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}
