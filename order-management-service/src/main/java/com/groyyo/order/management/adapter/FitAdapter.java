package com.groyyo.order.management.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.groyyo.core.master.dto.request.FitRequestDto;
import com.groyyo.core.master.dto.response.FitResponseDto;
import com.groyyo.order.management.entity.Fit;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FitAdapter {

	public Fit buildFitFromRequest(FitRequestDto fitRequestDto) {

		return Fit
				.builder()
				.name(fitRequestDto.getName())
				.masterId(fitRequestDto.getMasterId())
				.build();
	}

	public Fit buildFitFromResponse(FitResponseDto fitResponseDto) {

		return Fit
				.builder()
				.name(fitResponseDto.getName())
				.masterId(fitResponseDto.getMasterId())
				.build();
	}

	public Fit cloneFitWithRequest(FitRequestDto fitRequestDto, Fit fit) {

		if (StringUtils.isNotBlank(fitRequestDto.getName()))
			fit.setName(fitRequestDto.getName());

		if (StringUtils.isNotBlank(fitRequestDto.getMasterId()))
			fit.setMasterId(fitRequestDto.getMasterId());

		return fit;
	}

	public FitRequestDto buildRequestFromResponse(FitResponseDto fitResponseDto) {

		FitRequestDto fitRequestDto = FitRequestDto.builder().build();

		if (StringUtils.isNotBlank(fitResponseDto.getName()))
			fitRequestDto.setName(fitResponseDto.getName());

		if (StringUtils.isNotBlank(fitResponseDto.getMasterId()))
			fitRequestDto.setMasterId(fitResponseDto.getMasterId());

		return fitRequestDto;
	}

	public FitResponseDto buildResponseFromEntity(Fit fit) {

		return FitResponseDto
				.builder()
				.uuid(fit.getUuid())
				.name(fit.getName())
				.masterId(fit.getMasterId())
				.status(fit.isStatus())
				.build();
	}

	public List<FitResponseDto> buildResponsesFromEntities(List<Fit> fits) {

		return fits.stream().map(FitAdapter::buildResponseFromEntity).collect(Collectors.toList());
	}

}
