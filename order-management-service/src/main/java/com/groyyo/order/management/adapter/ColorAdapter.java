package com.groyyo.order.management.adapter;

import com.groyyo.core.master.dto.request.ColorRequestDto;
import com.groyyo.core.master.dto.response.ColorResponseDto;
import com.groyyo.order.management.entity.Color;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class ColorAdapter {

    public Color buildColorFromRequest(ColorRequestDto colorRequestDto) {

        return Color
                .builder()
                .name(colorRequestDto.getName())
                .hexCode(colorRequestDto.getHexCode())
                .build();
    }

    public Color buildColorFromResponse(ColorResponseDto colorResponseDto) {

        return Color
                .builder()
                .name(colorResponseDto.getName())
                .hexCode(colorResponseDto.getHexCode())
                .build();
    }

    public Color cloneColorWithRequest(ColorRequestDto colorRequestDto, Color color) {

        if (StringUtils.isNotBlank(colorRequestDto.getName()))
            color.setName(colorRequestDto.getName());

        if (StringUtils.isNotBlank(colorRequestDto.getHexCode()))
            color.setHexCode(colorRequestDto.getHexCode());

        return color;
    }

    public ColorRequestDto buildRequestFromResponse(ColorResponseDto colorResponseDto) {

        ColorRequestDto colorRequestDto = ColorRequestDto.builder().build();

        if (StringUtils.isNotBlank(colorResponseDto.getName()))
            colorRequestDto.setName(colorResponseDto.getName());
        if (StringUtils.isNotBlank(colorResponseDto.getHexCode()))
            colorRequestDto.setHexCode(colorResponseDto.getHexCode());

        return colorRequestDto;
    }

    public ColorResponseDto buildResponseFromEntity(Color color) {

        return ColorResponseDto
                .builder()
                .uuid(color.getUuid())
                .name(color.getName())
                .hexCode(color.getHexCode())
                .status(color.isStatus())
                .build();
    }

    public List<ColorResponseDto> buildResponsesFromEntities(List<Color> colors) {

        return colors.stream().map(ColorAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}
