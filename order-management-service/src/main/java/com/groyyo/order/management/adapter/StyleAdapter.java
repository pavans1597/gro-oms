package com.groyyo.order.management.adapter;
import com.groyyo.order.management.dto.request.StyleRequestDto;
import com.groyyo.order.management.dto.response.StyleResponseDto;
import com.groyyo.order.management.entity.Style;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class StyleAdapter {

    public Style buildStyleFromRequest(StyleRequestDto styleRequestDto) {

        return Style
                .builder()
                .name(styleRequestDto.getName())
                .styleNumber(styleRequestDto.getStyleNumber())
                .image(styleRequestDto.getImage())
                .cadImage(styleRequestDto.getCadImage())
                .productId(styleRequestDto.getProductId())
                .build();
    }

    public Style buildStyleFromResponse(StyleResponseDto styleResponseDto) {

        return Style
                .builder()
                .name(styleResponseDto.getName())
                .styleNumber(styleResponseDto.getStyleNumber())
                .image(styleResponseDto.getImage())
                .cadImage(styleResponseDto.getCadImage())
                .productId(styleResponseDto.getProductId())
                .build();
    }

    public Style cloneStyleWithRequest(StyleRequestDto styleRequestDto, Style style) {

        if (StringUtils.isNotBlank(styleRequestDto.getName()))
            style.setName(styleRequestDto.getName());
        if (StringUtils.isNotBlank(styleRequestDto.getStyleNumber()))
            style.setStyleNumber(styleRequestDto.getStyleNumber());
        if (StringUtils.isNotBlank(styleRequestDto.getImage()))
            style.setImage(styleRequestDto.getImage());
        if (StringUtils.isNotBlank(styleRequestDto.getCadImage()))
            style.setCadImage(styleRequestDto.getCadImage());
        if (StringUtils.isNotBlank(styleRequestDto.getProductId()))
            style.setProductId(styleRequestDto.getProductId());
        return style;
    }

    public StyleResponseDto buildResponseFromEntity(Style style) {

        return StyleResponseDto
                .builder()
                .uuid(style.getUuid())
                .name(style.getName())
                .image(style.getImage())
                .styleNumber(style.getStyleNumber())
                .cadImage(style.getCadImage())
                .productId(style.getProductId())
                .build();
    }

    public List<StyleResponseDto> buildResponsesFromEntities(List<Style> styles) {

        return styles.stream().map(StyleAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}
