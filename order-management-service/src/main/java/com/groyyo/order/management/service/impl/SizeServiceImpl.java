package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.groyyo.core.multitenancy.multitenancy.util.TenantContext;
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
import com.groyyo.core.master.dto.request.SizeRequestDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;
import com.groyyo.order.management.adapter.SizeAdapter;
import com.groyyo.order.management.db.service.SizeDbService;
import com.groyyo.order.management.entity.Size;
import com.groyyo.order.management.http.service.FactoryHttpService;
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

	@Autowired
	private FactoryHttpService factoryHttpService;

	@Override
	public List<SizeResponseDto> getAllSizes(Boolean status) {

		log.info("Serving request to get all sizes");
		
		String factoryId = TenantContext.getTenantId();

		List<Size> sizeEntities = Objects.isNull(status) ? sizeDbService.getAllSizes(factoryId)
				: sizeDbService.getAllSizesForStatus(status, factoryId);

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

		String factoryId = TenantContext.getTenantId();

		runValidations(sizeRequestDto, factoryId);

		Size size = SizeAdapter.buildSizeFromRequest(sizeRequestDto, factoryId);

		size = sizeDbService.saveSize(size);

		if (Objects.isNull(size)) {
			log.error("Unable to add size for object: {}", sizeRequestDto);
			return null;
		}

		return SizeAdapter.buildResponseFromEntity(size);
	}

	@Override
	public SizeResponseDto updateSize(SizeRequestDto sizeRequestDto) {

		log.info("Serving request to update a size with request object:{}", sizeRequestDto);

		Size size = sizeDbService.getSizeById(sizeRequestDto.getId());

		if (Objects.isNull(size)) {
			log.error("Size with id: {} not found in the system", sizeRequestDto.getId());
			return null;
		}

		runValidations(sizeRequestDto, null);

		size = SizeAdapter.cloneSizeWithRequest(sizeRequestDto, size);

		sizeDbService.saveSize(size);

		return SizeAdapter.buildResponseFromEntity(size);
	}

	@Override
	public SizeResponseDto conditionalSaveSize(SizeResponseDto sizeResponseDto) {

		log.info("Serving request to conditionally save a Size with response object: {}", sizeResponseDto);

		Size size = sizeDbService.findByNameAndFactoryId(sizeResponseDto.getName(), sizeResponseDto.getFactoryId());

		if (Objects.nonNull(size)) {
			log.error("Size already exists with name: {} and factory_id: {}", sizeResponseDto.getName(), sizeResponseDto.getFactoryId());
			return null;
		}

		size = SizeAdapter.buildSizeFromResponse(sizeResponseDto, sizeResponseDto.getFactoryId());

		size = sizeDbService.save(size);

		sizeResponseDto = SizeAdapter.buildResponseFromEntity(size);

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
		
		String factoryId = TenantContext.getTenantId();

		Size size = SizeAdapter.buildSizeFromResponse(sizeResponseDto, factoryId);

		if (Objects.isNull(size)) {
			log.error("Unable to build size from response object: {}", sizeResponseDto);
			return;
		}

		sizeDbService.saveSize(size);
	}

	@Override
	public void saveEntityFromCache(String factoryId, Map<String, SizeResponseDto> sizeByNameMap) {
		log.info("Populating size data for factory id: {}", factoryId);

		sizeByNameMap.values().forEach(sizeResponseDto -> {

			sizeResponseDto.setFactoryId(factoryId);

			conditionalSaveSize(sizeResponseDto);

		});
		/*List<String> factories = factoryHttpService.getFactoryIds();

		if (CollectionUtils.isEmpty(factories)) {
			log.error("No active factories found in the system to populate data for");
			return;
		}

		factories.forEach(factoryId -> {

			log.info("Populating size data for factory id: {}", factoryId);

			sizeByNameMap.values().forEach(sizeResponseDto -> {

				sizeResponseDto.setFactoryId(factoryId);

				conditionalSaveSize(sizeResponseDto);

			});
		});*/

	}

	private boolean isEntityExistsWithName(String name, String factoryId) {

		return StringUtils.isNotBlank(name) && StringUtils.isBlank(factoryId) ? sizeDbService.isEntityExistsByName(name) : Objects.nonNull(sizeDbService.findByNameAndFactoryId(name, factoryId));
	}

	private void runValidations(SizeRequestDto sizeRequestDto, String factoryId) {

		validateName(sizeRequestDto, factoryId);
	}

	private void validateName(SizeRequestDto sizeRequestDto, String factoryId) {

		if (isEntityExistsWithName(sizeRequestDto.getName(), factoryId)) {
			String errorMsg = "Size cannot be created/updated as record already exists with name: " + sizeRequestDto.getName() + " for factory id: " + factoryId;
			throw new RecordExistsException(errorMsg);
		}
	}

	@Override
	public Size findOrCreate(String name) {
		String factoryId = TenantContext.getTenantId();
		Size size = SizeAdapter.buildSizeFromName(name, factoryId);
		return sizeDbService.findOrCreate(size);
	}
}
