package com.groyyo.order.management.service.impl;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.fileManagement.dto.response.FileResponseDto;
import com.groyyo.core.enums.ServiceName;
import com.groyyo.core.file.management.client.api.FileManagementApi;
import com.groyyo.order.management.adapter.StyleAdapter;
import com.groyyo.order.management.db.service.StyleDbService;
import com.groyyo.order.management.dto.response.StyleDto;
import com.groyyo.order.management.entity.Style;
import com.groyyo.order.management.service.StyleService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
public class StyleServiceImpl implements StyleService {

	@Autowired
	private StyleDbService styleDbService;

	@Autowired
	private FileManagementApi fileManagementApi;

	@Override
	public List<StyleDto> getAllStyles(Boolean status) {
		log.info("Serving request to get all styles");
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		List<Style> styleEntities = Objects.isNull(status) ? styleDbService.getAllStyles(factoryId)
				: styleDbService.getAllStylesForStatus(status,factoryId);

		if (CollectionUtils.isEmpty(styleEntities)) {
			log.error("No Styles found in the system");
			return new ArrayList<>();
		}
		return styleEntities.stream().map(style -> {
			StyleDto styleDto = StyleAdapter.buildResponseFromEntity(style);
			setImagesForStyle(style, styleDto);
			return styleDto;
		}).collect(Collectors.toList());
	}

	@Override
	public StyleDto getStyleById(String id) {

		log.info("Serving request to get a style by id:{}", id);

		Style style = styleDbService.getStyleById(id);

		if (Objects.isNull(style)) {
			String errorMsg = "Style with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		return StyleAdapter.buildResponseFromEntity(style);
	}

	@Override
	public StyleDto addStyle(StyleDto styleRequestDto) {

		log.info("Serving request to add a style with request object:{}", styleRequestDto);

		runValidations(styleRequestDto);

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

		if (style.getStyleImageId() != null && !style.getStyleImageId().trim().isEmpty()) {
			ResponseDto<FileResponseDto> styleImage = fileManagementApi.getSignedUrl(style.getStyleImageId(), ServiceName.ORDER, true);
			styleDto.setStyleImage(StyleAdapter.buildImageDtoFrom(style.getStyleImageId(), Objects.nonNull(styleImage) ? styleImage.getData().getSignedUrl() : null));
		}

		if (style.getCadImageId() != null && !style.getCadImageId().trim().isEmpty()) {
			ResponseDto<FileResponseDto> cadImage = fileManagementApi.getSignedUrl(style.getCadImageId(), ServiceName.ORDER, true);
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

		runValidations(styleUpdateDto);

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

	private boolean isEntityExistsWithStyleNumber(String styleNumber) {

		return StringUtils.isNotBlank(styleNumber) && styleDbService.isEntityExistsByStyleNumber(styleNumber);
	}

	private void runValidations(StyleDto styleRequestDto) {
		validateStyleNumber(styleRequestDto);
	}

	private void validateStyleNumber(StyleDto styleRequestDto) {

		if (isEntityExistsWithStyleNumber(styleRequestDto.getStyleNumber())) {
			String errorMsg = "Style cannot be created/updated as record already exists with style number: " + styleRequestDto.getStyleNumber();
			throw new RecordExistsException(errorMsg);
		}
	}
}
