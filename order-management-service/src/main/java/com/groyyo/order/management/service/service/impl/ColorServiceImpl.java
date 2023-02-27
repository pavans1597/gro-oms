package com.groyyo.order.management.service.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.kafka.dto.KafkaDTO;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.core.master.dto.request.ColorRequestDto;
import com.groyyo.core.master.dto.response.ColorResponseDto;
import com.groyyo.order.management.service.adapter.ColorAdapter;
import com.groyyo.order.management.service.constants.KafkaConstants;
import com.groyyo.order.management.service.db.service.ColorDbService;
import com.groyyo.order.management.service.entity.Color;
import com.groyyo.order.management.service.service.ColorService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ColorServiceImpl implements ColorService {

    @Value("${kafka.master.updates.topic}")
    private String kafkaMasterDataUpdatesTopic;

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private ColorDbService colorDbService;

    @Override
    public List<ColorResponseDto> getAllColors(Boolean status) {

        log.info("Serving request to get all colors");

        List<Color> colorEntities = Objects.isNull(status) ? colorDbService.getAllColors()
                : colorDbService.getAllColorsForStatus(status);

        if (CollectionUtils.isEmpty(colorEntities)) {
            log.error("No Colors found in the system");
            return new ArrayList<ColorResponseDto>();
        }

        return ColorAdapter.buildResponsesFromEntities(colorEntities);
    }

    @Override
    public ColorResponseDto getColorById(String id) {

        log.info("Serving request to get a color by id: {}", id);

        Color color = colorDbService.getColorById(id);

        if (Objects.isNull(color)) {
            String errorMsg = "Color with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        return ColorAdapter.buildResponseFromEntity(color);
    }

    @Override
    public ColorResponseDto addColor(ColorRequestDto colorRequestDto) {

        log.info("Serving request to add a color with request object: {}", colorRequestDto);

        runValidations(colorRequestDto);

        Color color = ColorAdapter.buildColorFromRequest(colorRequestDto);

        color = colorDbService.saveColor(color);

        if (Objects.isNull(color)) {
            log.error("Unable to add color for object: {}", colorRequestDto);
            return null;
        }

        ColorResponseDto colorResponseDto = ColorAdapter.buildResponseFromEntity(color);

        publishColor(colorResponseDto, KafkaConstants.KAFKA_COLOR_TYPE, KafkaConstants.KAFKA_COLOR_SUBTYPE_CREATE, kafkaMasterDataUpdatesTopic);

        return colorResponseDto;
    }

    @Override
    public ColorResponseDto updateColor(ColorRequestDto colorRequestDto) {

        log.info("Serving request to update a color with request object: {}", colorRequestDto);

        Color color = colorDbService.getColorById(colorRequestDto.getId());

        if (Objects.isNull(color)) {
            log.error("Color with id: {} not found in the system", colorRequestDto.getId());
            return null;
        }

        runValidations(colorRequestDto);

        color = ColorAdapter.cloneColorWithRequest(colorRequestDto, color);

        colorDbService.saveColor(color);

        ColorResponseDto colorResponseDto = ColorAdapter.buildResponseFromEntity(color);

        publishColor(colorResponseDto, KafkaConstants.KAFKA_COLOR_TYPE, KafkaConstants.KAFKA_COLOR_SUBTYPE_UPDATE, kafkaMasterDataUpdatesTopic);

        return colorResponseDto;
    }

    @Override
    public ColorResponseDto activateDeactivateColor(String id, boolean status) {

        log.info("Serving request to activate / deactivate a color with id: {}", id);

        Color color = colorDbService.getColorById(id);

        if (Objects.isNull(color)) {
            String errorMsg = "Color with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        color = colorDbService.activateDeactivateColor(color, status);

        return ColorAdapter.buildResponseFromEntity(color);
    }

    private void publishColor(ColorResponseDto colorResponseDto, String type, String subType, String topicName) {

        KafkaDTO kafkaDTO = new KafkaDTO(type, subType, ColorResponseDto.class.getName(), colorResponseDto);
        notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
    }

    @Override
    public void consumeColor(ColorResponseDto colorResponseDto) {

        try {
            ColorRequestDto colorRequestDto = ColorAdapter.buildRequestFromResponse(colorResponseDto);

            if (Objects.isNull(colorRequestDto)) {
                log.error("Unable to build color request from response object: {}", colorResponseDto);
                return;
            }

            Color color = ColorAdapter.buildColorFromRequest(colorRequestDto);

            if (Objects.isNull(color)) {
                log.error("Unable to build color from request object: {}", colorRequestDto);
                return;
            }

            colorDbService.saveColor(color);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private boolean isEntityExistsWithName(String name) {

        return StringUtils.isNotBlank(name) && colorDbService.isEntityExistsByName(name);
    }

    private boolean isEntityExistsWithHexCode(String hexCode) {

        return StringUtils.isNotBlank(hexCode) && colorDbService.isEntityExistsByHexCode(hexCode);
    }

    private void runValidations(ColorRequestDto colorRequestDto) {

        validateName(colorRequestDto);
        validateHexCode(colorRequestDto);
    }

    private void validateName(ColorRequestDto colorRequestDto) {

        if (isEntityExistsWithName(colorRequestDto.getName())) {
            String errorMsg = "Color cannot be created/updated as record already exists with name: " + colorRequestDto.getName();
            throw new RecordExistsException(errorMsg);
        }
    }

    private void validateHexCode(ColorRequestDto colorRequestDto) {

        if (isEntityExistsWithHexCode(colorRequestDto.getHexCode())) {
            String errorMsg = "Color cannot be created/updated as record already exists with hexCode: " + colorRequestDto.getHexCode();
            throw new RecordExistsException(errorMsg);
        }
    }

}
