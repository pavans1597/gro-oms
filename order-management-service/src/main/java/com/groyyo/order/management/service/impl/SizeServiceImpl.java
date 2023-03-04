package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.groyyo.core.master.dto.request.SizeRequestDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;
import com.groyyo.order.management.adapter.SizeAdapter;
import com.groyyo.order.management.db.service.SizeDbService;
import com.groyyo.order.management.entity.Size;
import com.groyyo.order.management.service.SizeService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SizeServiceImpl implements SizeService {

	@Value("${kafka.base.topic}")
	private String kafkaMasterDataUpdatesTopic;

	@Autowired
	private NotificationProducer notificationProducer;

	@Autowired
	private SizeDbService sizeDbService;

	@Override
	public List<SizeResponseDto> getAllSizes(Boolean status) {

		log.info("Serving request to get all sizes");

		List<Size> sizeEntities = Objects.isNull(status) ? sizeDbService.getAllSizes()
				: sizeDbService.getAllSizesForStatus(status);

		if (CollectionUtils.isEmpty(sizeEntities)) {
			log.error("No Sizes found in the system");
			return new ArrayList<SizeResponseDto>();
		}

		return SizeAdapter.buildResponsesFromEntities(sizeEntities);
	}

	@Override
	public SizeResponseDto getSizeById(String id) {

		log.info("Serving request to get a size by id:{}", id);

		Size size = sizeDbService.getSizeById(id);

		if (Objects.isNull(size)) {
			String errorMsg = "Size with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		return SizeAdapter.buildResponseFromEntity(size);
	}

	@Override
	public SizeResponseDto addSize(SizeRequestDto sizeRequestDto) {

		log.info("Serving request to add a size with request object:{}", sizeRequestDto);

		runValidations(sizeRequestDto);

		Size size = SizeAdapter.buildSizeFromRequest(sizeRequestDto);

		size = sizeDbService.saveSize(size);

		if (Objects.isNull(size)) {
			log.error("Unable to add size for object: {}", sizeRequestDto);
			return null;
		}

		SizeResponseDto sizeResponseDto = SizeAdapter.buildResponseFromEntity(size);

		// publishSize(sizeResponseDto, KafkaConstants.KAFKA_SIZE_TYPE,
		// KafkaConstants.KAFKA_SIZE_SUBTYPE_CREATE, kafkaMasterDataUpdatesTopic);

		return sizeResponseDto;
	}

	@Override
	public SizeResponseDto updateSize(SizeRequestDto sizeRequestDto) {

		log.info("Serving request to update a size with request object:{}", sizeRequestDto);

		Size size = sizeDbService.getSizeById(sizeRequestDto.getId());

		if (Objects.isNull(size)) {
			log.error("Size with id: {} not found in the system", sizeRequestDto.getId());
			return null;
		}

		runValidations(sizeRequestDto);

		size = SizeAdapter.cloneSizeWithRequest(sizeRequestDto, size);

		sizeDbService.saveSize(size);

		SizeResponseDto sizeResponseDto = SizeAdapter.buildResponseFromEntity(size);

//        publishSize(sizeResponseDto, KafkaConstants.KAFKA_SIZE_TYPE, KafkaConstants.KAFKA_SIZE_SUBTYPE_UPDATE, kafkaMasterDataUpdatesTopic);

		return sizeResponseDto;
	}

	@Override
	public SizeResponseDto activateDeactivateSize(String id, boolean status) {

		log.info("Serving request to activate / deactivate a size with id:{}", id);

		Size size = sizeDbService.getSizeById(id);

		if (Objects.isNull(size)) {
			String errorMsg = "Size with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		size = sizeDbService.activateDeactivateSize(size, status);

		return SizeAdapter.buildResponseFromEntity(size);
	}

	@SuppressWarnings("unused")
	private void publishSize(SizeResponseDto sizeResponseDto, String type, String subType, String topicName) {

		KafkaDTO kafkaDTO = new KafkaDTO(type, subType, SizeResponseDto.class.getName(), sizeResponseDto);
		notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
	}

	@Override
	public void consumeSize(SizeResponseDto sizeResponseDto) {
		Size size = SizeAdapter.buildSizeFromResponse(sizeResponseDto);

		if (Objects.isNull(size)) {
			log.error("Unable to build size from response object: {}", sizeResponseDto);
			return;
		}

		sizeDbService.saveSize(size);
	}

	@Override
	public void saveEntityFromCache(Map<String, SizeResponseDto> sizeByNameMap) {

		sizeByNameMap.values().forEach(sizeResponseDto -> {

			SizeRequestDto sizeRequestDto = SizeAdapter.buildRequestFromResponse(sizeResponseDto);

			if (Objects.nonNull(sizeRequestDto)) {

				try {

					addSize(sizeRequestDto);

				} catch (Exception e) {

					log.error("Exception caught while saving size entity with data: {} from cache", sizeByNameMap, e);
				}
			}
		});

	}

	private boolean isEntityExistsWithName(String name) {

		return StringUtils.isNotBlank(name) && sizeDbService.isEntityExistsByName(name);
	}

	private void runValidations(SizeRequestDto sizeRequestDto) {

		validateName(sizeRequestDto);
	}

	private void validateName(SizeRequestDto sizeRequestDto) {

		if (isEntityExistsWithName(sizeRequestDto.getName())) {
			String errorMsg = "Size cannot be created/updated as record already exists with name: " + sizeRequestDto.getName();
			throw new RecordExistsException(errorMsg);
		}
	}
}
