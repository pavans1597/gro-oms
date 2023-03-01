package com.groyyo.order.service.impl;


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
import com.groyyo.core.master.dto.request.PartRequestDto;
import com.groyyo.core.master.dto.response.PartResponseDto;
import com.groyyo.order.adapter.PartAdapter;
import com.groyyo.order.constants.KafkaConstants;
import com.groyyo.order.db.service.PartDbService;
import com.groyyo.order.entity.Part;
import com.groyyo.order.service.PartService;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class PartServiceImpl implements PartService {

    @Value("${kafka.master.updates.topic}")
    private String kafkaMasterDataUpdatesTopic;

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private PartDbService partDbService;

    @Override
    public List<PartResponseDto> getAllParts(Boolean status) {

        log.info("Serving request to get all parts");

        List<Part> partEntities = Objects.isNull(status) ? partDbService.getAllParts()
                : partDbService.getAllPartsForStatus(status);

        if (CollectionUtils.isEmpty(partEntities)) {
            log.error("No Parts found in the system");
            return new ArrayList<PartResponseDto>();
        }

        return PartAdapter.buildResponsesFromEntities(partEntities);
    }

    @Override
    public PartResponseDto getPartById(String id) {

        log.info("Serving request to get a part by id:{}", id);

        Part part = partDbService.getPartById(id);

        if (Objects.isNull(part)) {
            String errorMsg = "Part with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        return PartAdapter.buildResponseFromEntity(part);
    }

    @Override
    public PartResponseDto addPart(PartRequestDto partRequestDto) {

        log.info("Serving request to add a part with request object:{}", partRequestDto);

        runValidations(partRequestDto);

        Part part = PartAdapter.buildPartFromRequest(partRequestDto);

        part = partDbService.savePart(part);

        if (Objects.isNull(part)) {
            log.error("Unable to add part for object: {}", partRequestDto);
            return null;
        }

        PartResponseDto partResponseDto = PartAdapter.buildResponseFromEntity(part);

        publishPart(partResponseDto, KafkaConstants.KAFKA_PART_TYPE, KafkaConstants.KAFKA_PART_SUBTYPE_CREATE, kafkaMasterDataUpdatesTopic);

        return partResponseDto;
    }

    @Override
    public PartResponseDto updatePart(PartRequestDto partRequestDto) {

        log.info("Serving request to update a part with request object:{}", partRequestDto);

        Part part = partDbService.getPartById(partRequestDto.getId());

        if (Objects.isNull(part)) {
            log.error("Part with id: {} not found in the system", partRequestDto.getId());
            return null;
        }

        runValidations(partRequestDto);

        part = PartAdapter.clonePartWithRequest(partRequestDto, part);

        partDbService.savePart(part);

        PartResponseDto partResponseDto = PartAdapter.buildResponseFromEntity(part);

        publishPart(partResponseDto, KafkaConstants.KAFKA_PART_TYPE, KafkaConstants.KAFKA_PART_SUBTYPE_UPDATE, kafkaMasterDataUpdatesTopic);

        return partResponseDto;
    }

    @Override
    public PartResponseDto activateDeactivatePart(String id, boolean status) {

        log.info("Serving request to activate / deactivate a part with id:{}", id);

        Part part = partDbService.getPartById(id);

        if (Objects.isNull(part)) {
            String errorMsg = "Part with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        part = partDbService.activateDeactivatePart(part, status);

        return PartAdapter.buildResponseFromEntity(part);
    }

    private void publishPart(PartResponseDto partResponseDto, String type, String subType, String topicName) {

        KafkaDTO kafkaDTO = new KafkaDTO(type, subType, PartResponseDto.class.getName(), partResponseDto);
        notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
    }

    @Override
    public void consumePart(PartResponseDto partResponseDto) {
        Part part = PartAdapter.buildPartFromResponse(partResponseDto);

        if (Objects.isNull(part)) {
            log.error("Unable to build part from response object: {}", partResponseDto);
            return;
        }

        partDbService.savePart(part);
    }

    private boolean isEntityExistsWithName(String name) {

        return StringUtils.isNotBlank(name) && partDbService.isEntityExistsByName(name);
    }

    private void runValidations(PartRequestDto partRequestDto) {

        validateName(partRequestDto);
    }

    private void validateName(PartRequestDto partRequestDto) {

        if (isEntityExistsWithName(partRequestDto.getName())) {
            String errorMsg = "Part cannot be created/updated as record already exists with name: " + partRequestDto.getName();
            throw new RecordExistsException(errorMsg);
        }
    }

}

