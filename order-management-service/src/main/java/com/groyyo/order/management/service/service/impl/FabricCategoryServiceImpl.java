package com.groyyo.order.management.service.service.impl;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.kafka.dto.KafkaDTO;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.order.management.service.adapter.FabricCategoryAdapter;
import com.groyyo.order.management.service.constants.KafkaConstants;
import com.groyyo.order.management.service.db.service.FabricCategoryDbService;
import com.groyyo.order.management.service.dto.request.FabricCategoryRequestDto;
import com.groyyo.order.management.service.dto.response.FabricCategoryResponseDto;
import com.groyyo.order.management.service.entity.FabricCategory;
import com.groyyo.order.management.service.service.FabricCategoryService;
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
public class FabricCategoryServiceImpl implements FabricCategoryService {

    @Value("${kafka.master.updates.topic}")
    private String kafkaMasterDataUpdatesTopic;

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private FabricCategoryDbService fabricCategoryDbService;

    @Override
    public List<FabricCategoryResponseDto> getAllFabricCategorys(Boolean status) {

        log.info("Serving request to get all fabricCategorys");

        List<FabricCategory> fabricCategoryEntities = Objects.isNull(status) ? fabricCategoryDbService.getAllFabricCategorys()
                : fabricCategoryDbService.getAllFabricCategorysForStatus(status);

        if (CollectionUtils.isEmpty(fabricCategoryEntities)) {
            log.error("No FabricCategorys found in the system");
            return new ArrayList<FabricCategoryResponseDto>();
        }

        return FabricCategoryAdapter.buildResponsesFromEntities(fabricCategoryEntities);
    }

    @Override
    public FabricCategoryResponseDto getFabricCategoryById(String id) {

        log.info("Serving request to get a fabricCategory by id: {}", id);

        FabricCategory fabricCategory = fabricCategoryDbService.getFabricCategoryById(id);

        if (Objects.isNull(fabricCategory)) {
            String errorMsg = "FabricCategory with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        return FabricCategoryAdapter.buildResponseFromEntity(fabricCategory);
    }

    @Override
    public FabricCategoryResponseDto addFabricCategory(FabricCategoryRequestDto fabricCategoryRequestDto) {

        log.info("Serving request to add a fabricCategory with request object: {}", fabricCategoryRequestDto);

        runValidations(fabricCategoryRequestDto);

        FabricCategory fabricCategory = FabricCategoryAdapter.buildFabricCategoryFromRequest(fabricCategoryRequestDto);

        fabricCategory = fabricCategoryDbService.saveFabricCategory(fabricCategory);

        if (Objects.isNull(fabricCategory)) {
            log.error("Unable to add fabricCategory for object: {}", fabricCategoryRequestDto);
            return null;
        }

        FabricCategoryResponseDto fabricCategoryResponseDto = FabricCategoryAdapter.buildResponseFromEntity(fabricCategory);

        publishFabricCategory(fabricCategoryResponseDto, KafkaConstants.KAFKA_COLOR_TYPE, KafkaConstants.KAFKA_COLOR_SUBTYPE_CREATE, kafkaMasterDataUpdatesTopic);

        return fabricCategoryResponseDto;
    }

    @Override
    public FabricCategoryResponseDto updateFabricCategory(FabricCategoryRequestDto fabricCategoryRequestDto) {

        log.info("Serving request to update a fabricCategory with request object: {}", fabricCategoryRequestDto);

        FabricCategory fabricCategory = fabricCategoryDbService.getFabricCategoryById(fabricCategoryRequestDto.getId());

        if (Objects.isNull(fabricCategory)) {
            log.error("FabricCategory with id: {} not found in the system", fabricCategoryRequestDto.getId());
            return null;
        }

        runValidations(fabricCategoryRequestDto);

        fabricCategory = FabricCategoryAdapter.cloneFabricCategoryWithRequest(fabricCategoryRequestDto, fabricCategory);

        fabricCategoryDbService.saveFabricCategory(fabricCategory);

        FabricCategoryResponseDto fabricCategoryResponseDto = FabricCategoryAdapter.buildResponseFromEntity(fabricCategory);

        publishFabricCategory(fabricCategoryResponseDto, KafkaConstants.KAFKA_COLOR_TYPE, KafkaConstants.KAFKA_COLOR_SUBTYPE_UPDATE, kafkaMasterDataUpdatesTopic);

        return fabricCategoryResponseDto;
    }

    @Override
    public FabricCategoryResponseDto activateDeactivateFabricCategory(String id, boolean status) {

        log.info("Serving request to activate / deactivate a fabricCategory with id: {}", id);

        FabricCategory fabricCategory = fabricCategoryDbService.getFabricCategoryById(id);

        if (Objects.isNull(fabricCategory)) {
            String errorMsg = "FabricCategory with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        fabricCategory = fabricCategoryDbService.activateDeactivateFabricCategory(fabricCategory, status);

        return FabricCategoryAdapter.buildResponseFromEntity(fabricCategory);
    }

    private void publishFabricCategory(FabricCategoryResponseDto fabricCategoryResponseDto, String type, String subType, String topicName) {

        KafkaDTO kafkaDTO = new KafkaDTO(type, subType, FabricCategoryResponseDto.class.getName(), fabricCategoryResponseDto);
        notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
    }

    @Override
    public void consumeFabricCategory(FabricCategoryResponseDto fabricCategoryResponseDto) {

        try {
            FabricCategoryRequestDto fabricCategoryRequestDto = FabricCategoryAdapter.buildRequestFromResponse(fabricCategoryResponseDto);

            if (Objects.isNull(fabricCategoryRequestDto)) {
                log.error("Unable to build fabricCategory request from response object: {}", fabricCategoryResponseDto);
                return;
            }

            FabricCategory fabricCategory = FabricCategoryAdapter.buildFabricCategoryFromRequest(fabricCategoryRequestDto);

            if (Objects.isNull(fabricCategory)) {
                log.error("Unable to build fabricCategory from request object: {}", fabricCategoryRequestDto);
                return;
            }

            fabricCategoryDbService.saveFabricCategory(fabricCategory);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private boolean isEntityExistsWithName(String name) {

        return StringUtils.isNotBlank(name) && fabricCategoryDbService.isEntityExistsByName(name);
    }

    private boolean isEntityExistsWithType(String type) {

        return StringUtils.isNotBlank(type) && fabricCategoryDbService.isEntityExistsByType(type);
    }

    private void runValidations(FabricCategoryRequestDto fabricCategoryRequestDto) {

        validateName(fabricCategoryRequestDto);
        validateType(fabricCategoryRequestDto);
    }

    private void validateName(FabricCategoryRequestDto fabricCategoryRequestDto) {

        if (isEntityExistsWithName(fabricCategoryRequestDto.getName())) {
            String errorMsg = "FabricCategory cannot be created/updated as record already exists with name: " + fabricCategoryRequestDto.getName();
            throw new RecordExistsException(errorMsg);
        }
    }

    private void validateType(FabricCategoryRequestDto fabricCategoryRequestDto) {

        if (isEntityExistsWithType(fabricCategoryRequestDto.getType())) {
            String errorMsg = "FabricCategory cannot be created/updated as record already exists with type: " + fabricCategoryRequestDto.getType();
            throw new RecordExistsException(errorMsg);
        }
    }

}


