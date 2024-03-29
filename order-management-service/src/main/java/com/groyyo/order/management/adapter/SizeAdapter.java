package com.groyyo.order.management.adapter;

import com.groyyo.core.master.dto.request.SizeRequestDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;
import com.groyyo.order.management.entity.Size;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SizeAdapter {

	public Size buildSizeFromRequest(SizeRequestDto sizeRequestDto,String factoryId) {

		return Size
				.builder()
				.name(sizeRequestDto.getName())
				.masterId(sizeRequestDto.getMasterId())
				.factoryId(factoryId)
				.build();
	}

	public Size buildSizeFromResponse(SizeResponseDto sizeResponseDto,String factoryId) {

		return Size
				.builder()
				.name(sizeResponseDto.getName())
				.masterId(sizeResponseDto.getMasterId())
				.factoryId(factoryId)
				.build();
	}

	public Size cloneSizeWithRequest(SizeRequestDto sizeRequestDto, Size size) {

		if (StringUtils.isNotBlank(sizeRequestDto.getName()))
			size.setName(sizeRequestDto.getName());

		if (StringUtils.isNotBlank(sizeRequestDto.getMasterId()))
			size.setMasterId(sizeRequestDto.getMasterId());

		return size;
	}

	public SizeRequestDto buildRequestFromResponse(SizeResponseDto sizeResponseDto) {

		SizeRequestDto sizeRequestDto = SizeRequestDto.builder().build();

		if (StringUtils.isNotBlank(sizeResponseDto.getName()))
			sizeRequestDto.setName(sizeResponseDto.getName());

		if (StringUtils.isNotBlank(sizeResponseDto.getMasterId()))
			sizeRequestDto.setMasterId(sizeResponseDto.getMasterId());

		return sizeRequestDto;
	}

	public SizeResponseDto buildResponseFromEntity(Size size) {

		return SizeResponseDto
				.builder()
				.uuid(size.getUuid())
				.name(size.getName())
				.masterId(size.getMasterId())
				.status(size.isStatus())
				.factoryId(size.getFactoryId())
				.build();
	}

	public List<SizeResponseDto> buildResponsesFromEntities(List<Size> sizes) {

		return sizes.stream().map(SizeAdapter::buildResponseFromEntity).collect(Collectors.toList());
	}

	public Size buildSizeFromName(String name, String factoryId) {
		return Size
				.builder()
				.name(name)
				.factoryId(factoryId)
				.build();
	}
}
