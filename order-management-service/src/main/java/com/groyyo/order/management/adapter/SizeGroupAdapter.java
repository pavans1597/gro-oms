package com.groyyo.order.management.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.groyyo.core.master.dto.request.SizeGroupRequestDto;
import com.groyyo.core.master.dto.response.SizeGroupResponseDto;
import com.groyyo.order.management.entity.SizeGroup;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SizeGroupAdapter {

	public SizeGroup buildSizeGroupFromRequest(SizeGroupRequestDto sizeGroupRequestDto) {

		return SizeGroup
				.builder()
				.name(sizeGroupRequestDto.getName())
				.sizeIds(sizeGroupRequestDto.getSizeIds())
				.masterId(sizeGroupRequestDto.getMasterId())
				.build();
	}

	public SizeGroup buildSizeGroupFromResponse(SizeGroupResponseDto sizeGroupResponseDto) {

		return SizeGroup
				.builder()
				.name(sizeGroupResponseDto.getName())
				.sizeIds(sizeGroupResponseDto.getSizeIds())
				.masterId(sizeGroupResponseDto.getMasterId())
				.build();
	}

	@SuppressWarnings("unchecked")
	public SizeGroup cloneSizeGroupWithRequest(SizeGroupRequestDto sizeGroupRequestDto, SizeGroup sizeGroup) {

		if (StringUtils.isNotBlank(sizeGroupRequestDto.getName()))
			sizeGroup.setName(sizeGroupRequestDto.getName());

		if (CollectionUtils.isNotEmpty(sizeGroupRequestDto.getSizeIds())) {

			if (sizeGroupRequestDto.isReplaceCall())
				sizeGroup.setSizeIds(sizeGroupRequestDto.getSizeIds());
			else
				sizeGroup.setSizeIds((List<String>) CollectionUtils.union(sizeGroup.getSizeIds(), sizeGroupRequestDto.getSizeIds()));
		}

		if (StringUtils.isNotBlank(sizeGroupRequestDto.getMasterId()))
			sizeGroup.setMasterId(sizeGroupRequestDto.getMasterId());

		return sizeGroup;
	}

	public SizeGroupRequestDto buildRequestFromResponse(SizeGroupResponseDto sizeGroupResponseDto) {

		SizeGroupRequestDto sizeGroupRequestDto = SizeGroupRequestDto.builder().build();

		if (StringUtils.isNotBlank(sizeGroupResponseDto.getName()))
			sizeGroupRequestDto.setName(sizeGroupResponseDto.getName());

		if (StringUtils.isNotBlank(sizeGroupResponseDto.getMasterId()))
			sizeGroupRequestDto.setMasterId(sizeGroupResponseDto.getMasterId());

		if (CollectionUtils.isNotEmpty(sizeGroupResponseDto.getSizeIds()))
			sizeGroupRequestDto.setSizeIds(sizeGroupResponseDto.getSizeIds());

		return sizeGroupRequestDto;
	}

	public SizeGroupResponseDto buildResponseFromEntity(SizeGroup sizeGroup) {

		return SizeGroupResponseDto
				.builder()
				.uuid(sizeGroup.getUuid())
				.name(sizeGroup.getName())
				.sizeIds(sizeGroup.getSizeIds())
				.masterId(sizeGroup.getMasterId())
				.status(sizeGroup.isStatus())
				.build();
	}

	public List<SizeGroupResponseDto> buildResponsesFromEntities(List<SizeGroup> sizeGroups) {

		return sizeGroups.stream().map(SizeGroupAdapter::buildResponseFromEntity).collect(Collectors.toList());
	}

}
