package com.groyyo.order.management.service.impl;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.kafka.dto.KafkaDTO;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.order.management.adapter.FabricAdapter;
import com.groyyo.order.management.constants.KafkaConstants;
import com.groyyo.order.management.db.service.FabricDbService;
import com.groyyo.order.management.dto.request.FabricRequestDto;
import com.groyyo.order.management.dto.response.FabricResponseDto;
import com.groyyo.order.management.entity.Fabric;
import com.groyyo.order.management.service.FabricService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
public class FabricServiceImpl implements FabricService {

    @Value("${kafka.master.updates.topic}")
    private String kafkaMasterDataUpdatesTopic;

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private FabricDbService fabricDbService;

    @Override
    public List<FabricResponseDto> getAllFabrics(Boolean status) {

        log.info("Serving request to get all fabrics");

        List<Fabric> fabricEntities = Objects.isNull(status) ? fabricDbService.getAllFabrics()
                : fabricDbService.getAllFabricsForStatus(status);

        if (CollectionUtils.isEmpty(fabricEntities)) {
            log.error("No Fabrics found in the system");
            return new ArrayList<FabricResponseDto>();
        }

        return FabricAdapter.buildResponsesFromEntities(fabricEntities);
    }

    @Override
    public FabricResponseDto getFabricById(String id) {

        log.info("Serving request to get a fabric by id: {}", id);

        Fabric fabric = fabricDbService.getFabricById(id);

        if (Objects.isNull(fabric)) {
            String errorMsg = "Fabric with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        return FabricAdapter.buildResponseFromEntity(fabric);
    }

    @Override
    public FabricResponseDto addFabric(FabricRequestDto fabricRequestDto) {

        log.info("Serving request to add a fabric with request object: {}", fabricRequestDto);

        runValidations(fabricRequestDto);

        Fabric fabric = FabricAdapter.buildFabricFromRequest(fabricRequestDto);

        fabric = fabricDbService.saveFabric(fabric);

        if (Objects.isNull(fabric)) {
            log.error("Unable to add fabric for object: {}", fabricRequestDto);
            return null;
        }

        FabricResponseDto fabricResponseDto = FabricAdapter.buildResponseFromEntity(fabric);

        publishFabric(fabricResponseDto, KafkaConstants.KAFKA_COLOR_TYPE, KafkaConstants.KAFKA_COLOR_SUBTYPE_CREATE, kafkaMasterDataUpdatesTopic);

        return fabricResponseDto;
    }

    @Override
    public FabricResponseDto updateFabric(FabricRequestDto fabricRequestDto) {

        log.info("Serving request to update a fabric with request object: {}", fabricRequestDto);

        Fabric fabric = fabricDbService.getFabricById(fabricRequestDto.getId());

        if (Objects.isNull(fabric)) {
            log.error("Fabric with id: {} not found in the system", fabricRequestDto.getId());
            return null;
        }

        runValidations(fabricRequestDto);

        fabric = FabricAdapter.cloneFabricWithRequest(fabricRequestDto, fabric);

        fabricDbService.saveFabric(fabric);

        FabricResponseDto fabricResponseDto = FabricAdapter.buildResponseFromEntity(fabric);

        publishFabric(fabricResponseDto, KafkaConstants.KAFKA_COLOR_TYPE, KafkaConstants.KAFKA_COLOR_SUBTYPE_UPDATE, kafkaMasterDataUpdatesTopic);

        return fabricResponseDto;
    }

    @Override
    public FabricResponseDto activateDeactivateFabric(String id, boolean status) {

        log.info("Serving request to activate / deactivate a fabric with id: {}", id);

        Fabric fabric = fabricDbService.getFabricById(id);

        if (Objects.isNull(fabric)) {
            String errorMsg = "Fabric with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        fabric = fabricDbService.activateDeactivateFabric(fabric, status);

        return FabricAdapter.buildResponseFromEntity(fabric);
    }

    private void publishFabric(FabricResponseDto fabricResponseDto, String fabricCategory, String subFabricCategory, String topicName) {

        KafkaDTO kafkaDTO = new KafkaDTO(fabricCategory, subFabricCategory, FabricResponseDto.class.getName(), fabricResponseDto);
        notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
    }

    @Override
    public void consumeFabric(FabricResponseDto fabricResponseDto) {

        try {
            FabricRequestDto fabricRequestDto = FabricAdapter.buildRequestFromResponse(fabricResponseDto);

            if (Objects.isNull(fabricRequestDto)) {
                log.error("Unable to build fabric request from response object: {}", fabricResponseDto);
                return;
            }

            Fabric fabric = FabricAdapter.buildFabricFromRequest(fabricRequestDto);

            if (Objects.isNull(fabric)) {
                log.error("Unable to build fabric from request object: {}", fabricRequestDto);
                return;
            }

            fabricDbService.saveFabric(fabric);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private boolean isEntityExistsWithName(String name) {

        return StringUtils.isNotBlank(name) && fabricDbService.isEntityExistsByName(name);
    }

    private boolean isEntityExistsWithFabricCategory(String fabricCategory) {

        return StringUtils.isNotBlank(fabricCategory) && fabricDbService.isEntityExistsByFabricCategory(fabricCategory);
    }

    private void runValidations(FabricRequestDto fabricRequestDto) {

        validateName(fabricRequestDto);
        validateFabricCategory(fabricRequestDto);
    }

    private void validateName(FabricRequestDto fabricRequestDto) {

        if (isEntityExistsWithName(fabricRequestDto.getName())) {
            String errorMsg = "Fabric cannot be created/updated as record already exists with name: " + fabricRequestDto.getName();
            throw new RecordExistsException(errorMsg);
        }
    }

    private void validateFabricCategory(FabricRequestDto fabricRequestDto) {

        if (isEntityExistsWithFabricCategory(fabricRequestDto.getFabricCategory())) {
            String errorMsg = "Fabric cannot be created/updated as record already exists with fabricCategory: " + fabricRequestDto.getFabricCategory();
            throw new RecordExistsException(errorMsg);
        }
    }

}

