package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.kafka.dto.KafkaDTO;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.core.master.dto.request.SizeGroupRequestDto;
import com.groyyo.core.master.dto.response.SizeGroupResponseDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;
import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import com.groyyo.order.management.adapter.SizeAdapter;
import com.groyyo.order.management.adapter.SizeGroupAdapter;
import com.groyyo.order.management.constants.KafkaConstants;
import com.groyyo.order.management.db.service.SizeDbService;
import com.groyyo.order.management.db.service.SizeGroupDbService;
import com.groyyo.order.management.entity.Size;
import com.groyyo.order.management.entity.SizeGroup;
import com.groyyo.order.management.http.service.FactoryHttpService;
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

	@Autowired
	private FactoryHttpService factoryHttpService;

	@Override
	public List<SizeGroupResponseDto> getAllSizeGroups(Boolean status) {

		log.info("Serving request to get all sizeGroups");
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		List<SizeGroup> sizeGroupEntities = Objects.isNull(status) ? sizeGroupDbService.getAllSizeGroups(factoryId)
				: sizeGroupDbService.getAllSizeGroupsForStatus(status, factoryId);

		if (CollectionUtils.isEmpty(sizeGroupEntities)) {
			log.error("No SizeGroups found in the system");
			return new ArrayList<SizeGroupResponseDto>();
		}

		List<SizeGroupResponseDto> sizeGroupResponseDtos = SizeGroupAdapter.buildResponsesFromEntities(sizeGroupEntities);

		populateSizeResponseMapInSizeGroupResponseDtos(sizeGroupResponseDtos);

		return sizeGroupResponseDtos;
	}

	@Override
	public SizeGroupResponseDto getSizeGroupById(String id) {

		log.info("Serving request to get a sizeGroup by id:{}", id);

		SizeGroup sizeGroup = sizeGroupDbService.getSizeGroupById(id);

		if (Objects.isNull(sizeGroup)) {
			String errorMsg = "SizeGroup with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		SizeGroupResponseDto sizeGroupResponseDto = SizeGroupAdapter.buildResponseFromEntity(sizeGroup);

		populateSizeResponseMapInSizeGroupResponseDtos(new ArrayList<SizeGroupResponseDto>(Arrays.asList(sizeGroupResponseDto)));

		return sizeGroupResponseDto;
	}

	@Override
	public SizeGroupResponseDto addSizeGroup(SizeGroupRequestDto sizeGroupRequestDto) {

		log.info("Serving request to add a sizeGroup with request object:{}", sizeGroupRequestDto);

		runValidations(sizeGroupRequestDto);
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		SizeGroup sizeGroup = SizeGroupAdapter.buildSizeGroupFromRequest(sizeGroupRequestDto, factoryId);

		sizeGroup = sizeGroupDbService.saveSizeGroup(sizeGroup);

		if (Objects.isNull(sizeGroup)) {
			log.error("Unable to add sizeGroup for object: {}", sizeGroupRequestDto);
			return null;
		}

		SizeGroupResponseDto sizeGroupResponseDto = SizeGroupAdapter.buildResponseFromEntity(sizeGroup);

		// publishSizeGroup(sizeGroupResponseDto, KafkaConstants.KAFKA_SIZE_GROUP_TYPE,
		// KafkaConstants.KAFKA_SIZE_GROUP_SUBTYPE_CREATE, kafkaMasterDataUpdatesTopic);

		populateSizeResponseMapInSizeGroupResponseDtos(new ArrayList<SizeGroupResponseDto>(Arrays.asList(sizeGroupResponseDto)));

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

		populateSizeResponseMapInSizeGroupResponseDtos(new ArrayList<SizeGroupResponseDto>(Arrays.asList(sizeGroupResponseDto)));

		return sizeGroupResponseDto;
	}

	@Override
	public SizeGroupResponseDto conditionalSaveSizeGroup(SizeGroupResponseDto sizeGroupResponseDto) {

		log.info("Serving request to conditionally save a SizeGroup with response object: {}", sizeGroupResponseDto);

		SizeGroup sizeGroup = sizeGroupDbService.findByNameAndFactoryId(sizeGroupResponseDto.getName(), sizeGroupResponseDto.getFactoryId());

		if (Objects.nonNull(sizeGroup)) {
			log.error("SizeGroup already exists with name: {} and factory_id: {}", sizeGroupResponseDto.getName(), sizeGroupResponseDto.getFactoryId());
			return null;
		}

		sizeGroup = SizeGroupAdapter.buildSizeGroupFromResponse(sizeGroupResponseDto, sizeGroupResponseDto.getFactoryId());

		sizeGroup = sizeGroupDbService.save(sizeGroup);

		sizeGroupResponseDto = SizeGroupAdapter.buildResponseFromEntity(sizeGroup);

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

		SizeGroupResponseDto sizeGroupResponseDto = SizeGroupAdapter.buildResponseFromEntity(sizeGroup);

		populateSizeResponseMapInSizeGroupResponseDtos(new ArrayList<SizeGroupResponseDto>(Arrays.asList(sizeGroupResponseDto)));

		return sizeGroupResponseDto;
	}

	private void publishSizeGroup(SizeGroupResponseDto sizeGroupResponseDto, String type, String subType, String topicName) {

		KafkaDTO kafkaDTO = new KafkaDTO(type, subType, SizeGroupResponseDto.class.getName(), sizeGroupResponseDto);
		notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
	}

	@Override
	public void consumeSizeGroup(SizeGroupResponseDto sizeGroupResponseDto) {
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		SizeGroup sizeGroup = SizeGroupAdapter.buildSizeGroupFromResponse(sizeGroupResponseDto, factoryId);

		if (Objects.isNull(sizeGroup)) {
			log.error("Unable to build sizeGroup from response object: {}", sizeGroupResponseDto);
			return;
		}

		sizeGroupDbService.saveSizeGroup(sizeGroup);
	}

	@Override
	public void saveEntityFromCache(String factoryId, Map<String, SizeGroupResponseDto> sizeGroupByNameMap) {
		log.info("Populating sizeGroup data for factory id: {}", factoryId);

		sizeGroupByNameMap.values().forEach(sizeGroupResponseDto -> {

			sizeGroupResponseDto.setFactoryId(factoryId);

			conditionalSaveSizeGroup(sizeGroupResponseDto);

		});

		/*List<String> factories = factoryHttpService.getFactoryIds();

		if (CollectionUtils.isEmpty(factories)) {
			log.error("No active factories found in the system to populate data for");
			return;
		}

		factories.forEach(factoryId -> {

			log.info("Populating sizeGroup data for factory id: {}", factoryId);

			sizeGroupByNameMap.values().forEach(sizeGroupResponseDto -> {

				sizeGroupResponseDto.setFactoryId(factoryId);

				conditionalSaveSizeGroup(sizeGroupResponseDto);

			});
		});*/
	}

	private boolean areSizeIdsValid(SizeGroupRequestDto sizeGroupRequestDto) {

		List<Size> sizes = sizeDbService.findByUuidInAndStatus(sizeGroupRequestDto.getSizeIds(), Boolean.TRUE);

		return sizes.size() > 0 && sizes.size() == sizeGroupRequestDto.getSizeIds().size();
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

	private void populateSizeResponseMapInSizeGroupResponseDtos(List<SizeGroupResponseDto> sizeGroupResponseDtos) {

		sizeGroupResponseDtos.forEach(sizeGroupResponseDto -> {

			sizeGroupResponseDto.setSizes(getSizesFromSizeIds(sizeGroupResponseDto));
		});
	}

	/**
	 * @param
	 * @return
	 */
	private Map<String, SizeResponseDto> getSizesFromSizeIds(SizeGroupResponseDto sizeGroupResponseDto) {

		List<Size> sizesForSizeIds = sizeDbService.findByUuidInAndStatus(sizeGroupResponseDto.getSizeIds(), Boolean.TRUE);

		Map<String, SizeResponseDto> sizeUuidResponseDtoMap = sizesForSizeIds.stream().collect(Collectors.toMap(size -> size.getUuid(), size -> SizeAdapter.buildResponseFromEntity(size)));

		return sizeUuidResponseDtoMap;
	}

	@Override
	public SizeGroup findOrCreate(String name, List<Size> sizes) {
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();
		SizeGroup sizeGroup = SizeGroupAdapter.buildSizeGroup(name, sizes.stream().map(AbstractJpaEntity::getUuid).collect(Collectors.toList()), factoryId);
		return sizeGroupDbService.findOrCreate(sizeGroup);
	}
}
