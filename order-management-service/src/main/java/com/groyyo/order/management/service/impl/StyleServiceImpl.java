package com.groyyo.order.management.service.impl;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.order.management.adapter.StyleAdapter;
import com.groyyo.order.management.db.service.StyleDbService;
import com.groyyo.order.management.dto.request.StyleRequestDto;
import com.groyyo.order.management.dto.request.StyleUpdateDto;
import com.groyyo.order.management.dto.response.StyleResponseDto;
import com.groyyo.order.management.entity.Style;
import com.groyyo.order.management.service.StyleService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
public class StyleServiceImpl implements StyleService {

    @Autowired
    private StyleDbService styleDbService;

    @Override
    public List<StyleResponseDto> getAllStyles(Boolean status) {

        log.info("Serving request to get all styles");

        List<Style> styleEntities = Objects.isNull(status) ? styleDbService.getAllStyles()
                : styleDbService.getAllStylesForStatus(status);

        if (CollectionUtils.isEmpty(styleEntities)) {
            log.error("No Styles found in the system");
            return new ArrayList<>();
        }

        return StyleAdapter.buildResponsesFromEntities(styleEntities);
    }

    @Override
    public StyleResponseDto getStyleById(String id) {

        log.info("Serving request to get a style by id:{}", id);

        Style style = styleDbService.getStyleById(id);

        if (Objects.isNull(style)) {
            String errorMsg = "Style with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        return StyleAdapter.buildResponseFromEntity(style);
    }

    @Override
    public StyleResponseDto addStyle(StyleRequestDto styleRequestDto) {

        log.info("Serving request to add a style with request object:{}", styleRequestDto);

        runValidations(styleRequestDto);

        Style style = StyleAdapter.buildStyleFromRequest(styleRequestDto);

        style = styleDbService.saveStyle(style);

        if (Objects.isNull(style)) {
            log.error("Unable to add style for object: {}", styleRequestDto);
            return null;
        }


        return StyleAdapter.buildResponseFromEntity(style);
    }

    @Override
    public StyleResponseDto updateStyle(StyleUpdateDto styleUpdateDto) {

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
    public StyleResponseDto activateDeactivateStyle(String id, boolean status) {

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
    public void consumeStyle(StyleResponseDto styleResponseDto) {
        Style style = StyleAdapter.buildStyleFromResponse(styleResponseDto);

        if (Objects.isNull(style)) {
            log.error("Unable to build style from response object: {}", styleResponseDto);
            return;
        }

        styleDbService.saveStyle(style);
    }

    private boolean isEntityExistsWithStyleNumber(String styleNumber) {

        return StringUtils.isNotBlank(styleNumber) && styleDbService.isEntityExistsByStyleNumber(styleNumber);
    }

    private void runValidations(StyleRequestDto styleRequestDto) {
        validateStyleNumber(styleRequestDto);
    }

    private void validateStyleNumber(StyleRequestDto styleRequestDto) {

        if (isEntityExistsWithStyleNumber(styleRequestDto.getStyleNumber())) {
            String errorMsg = "Style cannot be created/updated as record already exists with style number: " + styleRequestDto.getStyleNumber();
            throw new RecordExistsException(errorMsg);
        }
    }
}
