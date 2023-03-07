package com.groyyo.order.management.adapter;

import com.groyyo.order.management.dto.request.ImageDto;
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
                .styleImageId(styleRequestDto.getStyleImageId())
                .cadImageId(styleRequestDto.getCadImageId())
                .productId(styleRequestDto.getProductId())
                .productName(styleRequestDto.getProductName())
                .build();
    }

    public Style buildStyleFromResponse(StyleResponseDto styleResponseDto) {

        return Style
                .builder()
                .name(styleResponseDto.getName())
                .styleNumber(styleResponseDto.getStyleNumber())
                .styleImageId(styleResponseDto.getStyleImage().getImgId())
                .cadImageId(styleResponseDto.getCadImage().getImgId())
                .productId(styleResponseDto.getProductId())
                .build();
    }

    public Style cloneStyleWithRequest(StyleRequestDto styleRequestDto, Style style) {

        if (StringUtils.isNotBlank(styleRequestDto.getName()))
            style.setName(styleRequestDto.getName());
        if (StringUtils.isNotBlank(styleRequestDto.getStyleNumber()))
            style.setStyleNumber(styleRequestDto.getStyleNumber());
        if (StringUtils.isNotBlank(styleRequestDto.getStyleImageId()))
            style.setStyleImageId(styleRequestDto.getStyleImageId());
        if (StringUtils.isNotBlank(styleRequestDto.getCadImageId()))
            style.setCadImageId(styleRequestDto.getCadImageId());
        if (StringUtils.isNotBlank(styleRequestDto.getProductId()))
            style.setProductId(styleRequestDto.getProductId());
        return style;
    }

    public StyleResponseDto buildResponseFromEntity(Style style) {

        return StyleResponseDto
                .builder()
                .uuid(style.getUuid())
                .name(style.getName())
                .styleNumber(style.getStyleNumber())
                .productId(style.getProductId())
                .productName(style.getProductName())
                .build();
    }

    public List<StyleResponseDto> buildResponsesFromEntities(List<Style> styles) {
        return styles.stream().map(StyleAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

    public ImageDto buildImageDtoFrom(String imageId, String imageUrl) {
        return ImageDto
                .builder()
                .imgId(imageId)
                .imgUrl(imageUrl)
                .build();
    }

}
