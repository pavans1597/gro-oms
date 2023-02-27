package com.groyyo.order.management.service.adapter;

import com.groyyo.core.master.dto.request.FitRequestDto;
import com.groyyo.core.master.dto.response.FitResponseDto;
import com.groyyo.order.management.service.entity.Fit;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class FitAdapter {

    public Fit buildFitFromRequest(FitRequestDto fitRequestDto) {

        return Fit
                .builder()
                .name(fitRequestDto.getName())
                .build();
    }

    public Fit buildFitFromResponse(FitResponseDto fitResponseDto) {

        return Fit
                .builder()
                .name(fitResponseDto.getName())
                .build();
    }

    public Fit cloneFitWithRequest(FitRequestDto fitRequestDto, Fit fit) {

        if (StringUtils.isNotBlank(fitRequestDto.getName()))
            fit.setName(fitRequestDto.getName());

        return fit;
    }

    public FitResponseDto buildResponseFromEntity(Fit fit) {

        return FitResponseDto
                .builder()
                .uuid(fit.getUuid())
                .name(fit.getName())
                .status(fit.isStatus())
                .build();
    }

    public List<FitResponseDto> buildResponsesFromEntities(List<Fit> fits) {

        return fits.stream().map(FitAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}
