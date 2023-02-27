package com.groyyo.order.management.service.adapter;

import com.groyyo.core.master.dto.request.SizeRequestDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;
import com.groyyo.order.management.service.entity.Size;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class SizeAdapter {

    public Size buildSizeFromRequest(SizeRequestDto sizeRequestDto) {

        return Size
                .builder()
                .name(sizeRequestDto.getName())
                .build();
    }

    public Size buildSizeFromResponse(SizeResponseDto sizeResponseDto) {

        return Size
                .builder()
                .name(sizeResponseDto.getName())
                .build();
    }

    public Size cloneSizeWithRequest(SizeRequestDto sizeRequestDto, Size size) {

        if (StringUtils.isNotBlank(sizeRequestDto.getName()))
            size.setName(sizeRequestDto.getName());

        return size;
    }

    public SizeResponseDto buildResponseFromEntity(Size size) {

        return SizeResponseDto
                .builder()
                .uuid(size.getUuid())
                .name(size.getName())
                .status(size.isStatus())
                .build();
    }

    public List<SizeResponseDto> buildResponsesFromEntities(List<Size> sizes) {

        return sizes.stream().map(SizeAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}
