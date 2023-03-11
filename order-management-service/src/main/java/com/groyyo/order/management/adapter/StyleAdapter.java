package com.groyyo.order.management.adapter;

import com.groyyo.order.management.dto.request.ImageDto;
import com.groyyo.order.management.dto.response.StyleDto;
import com.groyyo.order.management.entity.Style;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class StyleAdapter {

	@SuppressWarnings("rawtypes")
	public Style buildStyleFromRequest(StyleDto styleDto) {

		Style.StyleBuilder builder = Style.builder()
				.name(styleDto.getName())
				.styleNumber(styleDto.getStyleNumber())
				.productId(styleDto.getProductId())
				.productName(styleDto.getProductName());

		if (styleDto.getStyleImage() != null) {
			builder.styleImageId(styleDto.getStyleImage().getImgId());
		}

		if (styleDto.getCadImage() != null) {
			builder.cadImageId(styleDto.getCadImage().getImgId());
		}

		return builder.build();
	}

	public Style buildStyleFromResponse(StyleDto styleDto) {

		return Style
				.builder()
				.name(styleDto.getName())
				.styleNumber(styleDto.getStyleNumber())
				.styleImageId(styleDto.getStyleImage().getImgId())
				.cadImageId(styleDto.getCadImage().getImgId())
				.productId(styleDto.getProductId())
				.build();
	}

	public Style cloneStyleWithRequest(StyleDto styleRequestDto, Style style) {

		if (StringUtils.isNotBlank(styleRequestDto.getName()))
			style.setName(styleRequestDto.getName());
		if (StringUtils.isNotBlank(styleRequestDto.getStyleNumber()))
			style.setStyleNumber(styleRequestDto.getStyleNumber());
		if (StringUtils.isNotBlank(styleRequestDto.getStyleImage().getImgId()))
			style.setStyleImageId(styleRequestDto.getStyleImage().getImgId());
		if (StringUtils.isNotBlank(styleRequestDto.getCadImage().getImgId()))
			style.setCadImageId(styleRequestDto.getCadImage().getImgId());
		if (StringUtils.isNotBlank(styleRequestDto.getProductId()))
			style.setProductId(styleRequestDto.getProductId());
		return style;
	}

	public StyleDto buildResponseFromEntity(Style style) {

		return StyleDto
				.builder()
				.uuid(style.getUuid())
				.name(style.getName())
				.styleNumber(style.getStyleNumber())
				.productId(style.getProductId())
				.productName(style.getProductName())
				.factoryId(style.getFactoryId())
				.build();
	}

	public List<StyleDto> buildResponsesFromEntities(List<Style> styles) {
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
