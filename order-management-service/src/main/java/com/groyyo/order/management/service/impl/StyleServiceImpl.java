package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.groyyo.core.base.exception.GroyyoException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.PurchaseOrder.StyleDto;
import com.groyyo.core.dto.fileManagement.dto.response.FileResponseDto;
import com.groyyo.order.management.adapter.StyleAdapter;
import com.groyyo.order.management.db.service.StyleDbService;
import com.groyyo.order.management.entity.Product;
import com.groyyo.order.management.entity.Style;
import com.groyyo.order.management.http.service.FileManagementHttpService;
import com.groyyo.order.management.service.StyleService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class StyleServiceImpl implements StyleService {

	@Autowired
	private StyleDbService styleDbService;

	@Autowired
	private FileManagementHttpService fileManagementHttpService;

	@Override
	public List<StyleDto> getAllStyles(Boolean status) {
		log.info("Serving request to get all styles");
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		List<Style> styleEntities = Objects.isNull(status) ? styleDbService.getAllStyles(factoryId)
				: styleDbService.getAllStylesForStatus(status, factoryId);

		if (CollectionUtils.isEmpty(styleEntities)) {
			log.error("No Styles found in the system");
			return new ArrayList<>();
		}

		return buildResponsesWithImagesForEntities(styleEntities);
	}

	private List<StyleDto> buildResponsesWithImagesForEntities(List<Style> styleEntities) {

		return styleEntities.stream().map(style -> buildResponseWithImagesForEntity(style)).collect(Collectors.toList());
	}

	private StyleDto buildResponseWithImagesForEntity(Style style) {

		StyleDto styleDto = StyleAdapter.buildResponseFromEntity(style);
		setImagesForStyle(style, styleDto);

		return styleDto;
	}

	@Override
	public StyleDto getStyleById(String id) {

		log.info("Serving request to get a style by id:{}", id);

		Style style = styleDbService.getStyleById(id);

		if (Objects.isNull(style)) {
			String errorMsg = "Style with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		StyleDto styleDto = StyleAdapter.buildResponseFromEntity(style);

		setImagesForStyle(style, styleDto);

		return styleDto;
	}

	@Override
	public StyleDto addStyle(StyleDto styleRequestDto) {

		log.info("Serving request to add a style with request object:{}", styleRequestDto);

		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		runValidations(styleRequestDto, factoryId);

		Style style = StyleAdapter.buildStyleFromRequest(styleRequestDto);

		style = styleDbService.saveStyle(style);

		if (Objects.isNull(style)) {
			log.error("Unable to add style for object: {}", styleRequestDto);
			return null;
		}

		StyleDto styleDto = StyleAdapter.buildResponseFromEntity(style);
		setImagesForStyle(style, styleDto);

		return styleDto;
	}

	private void setImagesForStyle(Style style, StyleDto styleDto) {

		if (Objects.isNull(style)) {
			return;
		}

		if (StringUtils.isNotBlank(style.getStyleImageId())) {
			ResponseDto<FileResponseDto> styleImage = fileManagementHttpService.getStyleImage(style.getStyleImageId());

			if (Objects.nonNull(styleImage))
				styleDto.setStyleImage(StyleAdapter.buildImageDtoFrom(style.getStyleImageId(), Objects.nonNull(styleImage) ? styleImage.getData().getSignedUrl() : null));
		}

		if (StringUtils.isNotBlank(style.getCadImageId())) {
			ResponseDto<FileResponseDto> cadImage = fileManagementHttpService.getStyleImage(style.getCadImageId());

			if (Objects.nonNull(cadImage))
				styleDto.setCadImage(StyleAdapter.buildImageDtoFrom(style.getCadImageId(), Objects.nonNull(cadImage.getData()) ? cadImage.getData().getSignedUrl() : null));
		}
	}

	@Override
	public StyleDto updateStyle(StyleDto styleUpdateDto) {

		log.info("Serving request to update a style with request object:{}", styleUpdateDto);

		Style style = styleDbService.getStyleById(styleUpdateDto.getId());

		if (Objects.isNull(style)) {
			log.error("Style with id: {} not found in the system", styleUpdateDto.getId());
			return null;
		}

		runValidations(styleUpdateDto, null);

		style = StyleAdapter.cloneStyleWithRequest(styleUpdateDto, style);

		styleDbService.saveStyle(style);

		return StyleAdapter.buildResponseFromEntity(style);
	}

	@Override
	public StyleDto activateDeactivateStyle(String id, boolean status) {

		log.info("Serving request to activate / deactivate a style with id:{}", id);

		Style style = styleDbService.getStyleById(id);

		if (Objects.isNull(style)) {
			String errorMsg = "Style with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		style = styleDbService.activateDeactivateStyle(style, status);

		return StyleAdapter.buildResponseFromEntity(style);
	}

	@Override
	public void consumeStyle(StyleDto styleDto) {
		Style style = StyleAdapter.buildStyleFromResponse(styleDto);

		if (Objects.isNull(style)) {
			log.error("Unable to build style from response object: {}", styleDto);
			return;
		}

		styleDbService.saveStyle(style);
	}

	private boolean isEntityExistsWithStyleNumber(String styleNumber, String factoryId) {

		return StringUtils.isNotBlank(styleNumber) && StringUtils.isBlank(factoryId) ? styleDbService.isEntityExistsByStyleNumber(styleNumber)
				: styleDbService.isEntityExistsByStyleNumberAndFactoryId(styleNumber, factoryId);
	}

	private void runValidations(StyleDto styleRequestDto, String factoryId) {

		validateStyleNumber(styleRequestDto, factoryId);
	}

	private void validateStyleNumber(StyleDto styleRequestDto, String factoryId) {

		if (isEntityExistsWithStyleNumber(styleRequestDto.getStyleNumber(), factoryId)) {
			String errorMsg = "Style cannot be created/updated as record already exists with style number: " + styleRequestDto.getStyleNumber() + " for factory id: " + factoryId;
			throw new RecordExistsException(errorMsg);
		}
	}

	@Override
	public Style findOrCreate(String name, String styleNumber, Product product) {
			String factoryId = HeaderUtil.getFactoryIdHeaderValue();
			Style style = StyleAdapter.buildStyleFromName(name, styleNumber, product, factoryId);
			return styleDbService.findOrCreate(style);
	}
}
