package com.groyyo.order.management.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.groyyo.order.management.constants.KafkaConstants;
import com.groyyo.order.management.entity.Size;
import com.groyyo.order.management.entity.SizeGroup;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.kafka.dto.KafkaDTO;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.core.master.dto.request.SizeGroupRequestDto;
import com.groyyo.core.master.dto.response.SizeGroupResponseDto;
import com.groyyo.order.management.adapter.SizeGroupAdapter;
import com.groyyo.order.management.db.service.SizeDbService;
import com.groyyo.order.management.db.service.SizeGroupDbService;
import com.groyyo.order.management.service.SizeGroupService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SizeGroupServiceImpl implements SizeGroupService {

    @Value("${kafka.master.updates.topic}")
    private String kafkaMasterDataUpdatesTopic;

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private SizeGroupDbService sizeGroupDbService;

    @Autowired
    private SizeDbService sizeDbService;

    @Override
    public List<SizeGroupResponseDto> getAllSizeGroups(Boolean status) {

        log.info("Serving request to get all sizeGroups");

        List<SizeGroup> sizeGroupEntities = Objects.isNull(status) ? sizeGroupDbService.getAllSizeGroups()
                : sizeGroupDbService.getAllSizeGroupsForStatus(status);

        if (CollectionUtils.isEmpty(sizeGroupEntities)) {
            log.error("No SizeGroups found in the system");
            return new ArrayList<SizeGroupResponseDto>();
        }

        return SizeGroupAdapter.buildResponsesFromEntities(sizeGroupEntities);
    }

    @Override
    public SizeGroupResponseDto getSizeGroupById(String id) {

        log.info("Serving request to get a sizeGroup by id:{}", id);

        SizeGroup sizeGroup = sizeGroupDbService.getSizeGroupById(id);

        if (Objects.isNull(sizeGroup)) {
            String errorMsg = "SizeGroup with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        return SizeGroupAdapter.buildResponseFromEntity(sizeGroup);
    }

    @Override
    public SizeGroupResponseDto addSizeGroup(SizeGroupRequestDto sizeGroupRequestDto) {

        log.info("Serving request to add a sizeGroup with request object:{}", sizeGroupRequestDto);

        runValidations(sizeGroupRequestDto);

        SizeGroup sizeGroup = SizeGroupAdapter.buildSizeGroupFromRequest(sizeGroupRequestDto);

        sizeGroup = sizeGroupDbService.saveSizeGroup(sizeGroup);

        if (Objects.isNull(sizeGroup)) {
            log.error("Unable to add sizeGroup for object: {}", sizeGroupRequestDto);
            return null;
        }

        SizeGroupResponseDto sizeGroupResponseDto = SizeGroupAdapter.buildResponseFromEntity(sizeGroup);

        publishSizeGroup(sizeGroupResponseDto, KafkaConstants.KAFKA_SIZE_GROUP_TYPE, KafkaConstants.KAFKA_SIZE_GROUP_SUBTYPE_CREATE, kafkaMasterDataUpdatesTopic);

        return sizeGroupResponseDto;
    }

    @Override
    public SizeGroupResponseDto updateSizeGroup(SizeGroupRequestDto sizeGroupRequestDto) {

        log.info("Serving request to update a sizeGroup with request object:{}", sizeGroupRequestDto);

        SizeGroup sizeGroup = sizeGroupDbService.getSizeGroupById(sizeGroupRequestDto.getId());

        if (Objects.isNull(sizeGroup)) {
            log.error("SizeGroup with id: {} not found in the system", sizeGroupRequestDto.getId());
            return null;
        }

        runValidations(sizeGroupRequestDto);

        sizeGroup = SizeGroupAdapter.cloneSizeGroupWithRequest(sizeGroupRequestDto, sizeGroup);

        sizeGroupDbService.saveSizeGroup(sizeGroup);

        SizeGroupResponseDto sizeGroupResponseDto = SizeGroupAdapter.buildResponseFromEntity(sizeGroup);

        publishSizeGroup(sizeGroupResponseDto, KafkaConstants.KAFKA_SIZE_GROUP_TYPE, KafkaConstants.KAFKA_SIZE_GROUP_SUBTYPE_UPDATE, kafkaMasterDataUpdatesTopic);

        return sizeGroupResponseDto;
    }

    @Override
    public SizeGroupResponseDto activateDeactivateSizeGroup(String id, boolean status) {

        log.info("Serving request to activate / deactivate a sizeGroup with id:{}", id);

        SizeGroup sizeGroup = sizeGroupDbService.getSizeGroupById(id);

        if (Objects.isNull(sizeGroup)) {
            String errorMsg = "SizeGroup with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        sizeGroup = sizeGroupDbService.activateDeactivateSizeGroup(sizeGroup, status);

        return SizeGroupAdapter.buildResponseFromEntity(sizeGroup);
    }

    private void publishSizeGroup(SizeGroupResponseDto sizeGroupResponseDto, String type, String subType, String topicName) {

        KafkaDTO kafkaDTO = new KafkaDTO(type, subType, SizeGroupResponseDto.class.getName(), sizeGroupResponseDto);
        notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
    }

    @Override
    public void consumeSizeGroup(SizeGroupResponseDto sizeGroupResponseDto) {
        SizeGroup sizeGroup = SizeGroupAdapter.buildSizeGroupFromResponse(sizeGroupResponseDto);

        if (Objects.isNull(sizeGroup)) {
            log.error("Unable to build sizeGroup from response object: {}", sizeGroupResponseDto);
            return;
        }

        sizeGroupDbService.saveSizeGroup(sizeGroup);
    }

    private boolean areSizeIdsValid(SizeGroupRequestDto sizeGroupRequestDto) {

        List<Size> sizes = sizeDbService.findByUuidInAndStatus(sizeGroupRequestDto.getSizeIds(), Boolean.TRUE);

        return sizes.size() == sizeGroupRequestDto.getSizeIds().size();
    }

    private boolean isEntityExistsWithName(String name) {

        return StringUtils.isNotBlank(name) && sizeGroupDbService.isEntityExistsByName(name);
    }

    private void runValidations(SizeGroupRequestDto sizeGroupRequestDto) {

        validateName(sizeGroupRequestDto);
        validateSizeIds(sizeGroupRequestDto);
    }

    private void validateName(SizeGroupRequestDto sizeGroupRequestDto) {

        if (isEntityExistsWithName(sizeGroupRequestDto.getName())) {
            String errorMsg = "SizeGroup cannot be created/updated as record already exists with name: " + sizeGroupRequestDto.getName();
            throw new RecordExistsException(errorMsg);
        }
    }

    private void validateSizeIds(SizeGroupRequestDto sizeGroupRequestDto) {

        if (!areSizeIdsValid(sizeGroupRequestDto)) {
            String errorMsg = "SizeIds should be the ids for sizes in the system. Received few/all invalid sizes in the request: " + sizeGroupRequestDto;
            throw new NoRecordException(errorMsg);
        }
    }
}

