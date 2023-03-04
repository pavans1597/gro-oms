package com.groyyo.order.management.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.groyyo.core.master.dto.request.PartRequestDto;
import com.groyyo.core.master.dto.response.PartResponseDto;
import com.groyyo.order.management.entity.Part;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PartAdapter {

	public Part buildPartFromRequest(PartRequestDto partRequestDto) {

		return Part
				.builder()
				.name(partRequestDto.getName())
				.masterId(partRequestDto.getMasterId())
				.build();
	}

	public Part buildPartFromResponse(PartResponseDto partResponseDto) {

		return Part
				.builder()
				.name(partResponseDto.getName())
				.masterId(partResponseDto.getMasterId())
				.build();
	}

	public Part clonePartWithRequest(PartRequestDto partRequestDto, Part part) {

		if (StringUtils.isNotBlank(partRequestDto.getName()))
			part.setName(partRequestDto.getName());

		if (StringUtils.isNotBlank(partRequestDto.getMasterId()))
			part.setMasterId(partRequestDto.getMasterId());

		return part;
	}

	public PartRequestDto buildRequestFromResponse(PartResponseDto partResponseDto) {

		PartRequestDto partRequestDto = PartRequestDto.builder().build();

		if (StringUtils.isNotBlank(partResponseDto.getName()))
			partRequestDto.setName(partResponseDto.getName());

		if (StringUtils.isNotBlank(partResponseDto.getMasterId()))
			partRequestDto.setMasterId(partResponseDto.getMasterId());

		return partRequestDto;
	}

	public PartResponseDto buildResponseFromEntity(Part part) {

		return PartResponseDto
				.builder()
				.uuid(part.getUuid())
				.name(part.getName())
				.masterId(part.getMasterId())
				.status(part.isStatus())
				.build();
	}

	public List<PartResponseDto> buildResponsesFromEntities(List<Part> parts) {

		return parts.stream().map(PartAdapter::buildResponseFromEntity).collect(Collectors.toList());
	}

}
