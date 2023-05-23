package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.groyyo.core.base.exception.GroyyoException;
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
import com.groyyo.core.master.dto.request.FitRequestDto;
import com.groyyo.core.master.dto.response.FitResponseDto;
import com.groyyo.order.management.adapter.FitAdapter;
import com.groyyo.order.management.db.service.FitDbService;
import com.groyyo.order.management.entity.Fit;
import com.groyyo.order.management.http.service.FactoryHttpService;
import com.groyyo.order.management.service.FitService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FitServiceImpl implements FitService {

	@Value("${kafka.master.updates.topic}")
	private String kafkaMasterDataUpdatesTopic;

	@Autowired
	private NotificationProducer notificationProducer;

	@Autowired
	private FitDbService fitDbService;

	@Autowired
	private FactoryHttpService factoryHttpService;

	@Override
	public List<FitResponseDto> getAllFits(Boolean status) {

		log.info("Serving request to get all fits");
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		List<Fit> fitEntities = Objects.isNull(status) ? fitDbService.getAllFits(factoryId)
				: fitDbService.getAllFitsForStatus(status, factoryId);

		if (CollectionUtils.isEmpty(fitEntities)) {
			log.error("No Fits found in the system");
			return new ArrayList<FitResponseDto>();
		}

		return FitAdapter.buildResponsesFromEntities(fitEntities);
	}

	@Override
	public FitResponseDto getFitById(String id) {

		log.info("Serving request to get a fit by id:{}", id);

		Fit fit = fitDbService.getFitById(id);

		if (Objects.isNull(fit)) {
			String errorMsg = "Fit with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		return FitAdapter.buildResponseFromEntity(fit);
	}

	@Override
	public FitResponseDto addFit(FitRequestDto fitRequestDto) {

		log.info("Serving request to add a fit with request object:{}", fitRequestDto);

		runValidations(fitRequestDto);
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		Fit fit = FitAdapter.buildFitFromRequest(fitRequestDto, factoryId);

		fit = fitDbService.saveFit(fit);

		if (Objects.isNull(fit)) {
			log.error("Unable to add fit for object: {}", fitRequestDto);
			return null;
		}

		return FitAdapter.buildResponseFromEntity(fit);
	}

	@Override
	public FitResponseDto updateFit(FitRequestDto fitRequestDto) {

		log.info("Serving request to update a fit with request object:{}", fitRequestDto);

		Fit fit = fitDbService.getFitById(fitRequestDto.getId());

		if (Objects.isNull(fit)) {
			log.error("Fit with id: {} not found in the system", fitRequestDto.getId());
			return null;
		}

		runValidations(fitRequestDto);

		fit = FitAdapter.cloneFitWithRequest(fitRequestDto, fit);

		fitDbService.saveFit(fit);

		return FitAdapter.buildResponseFromEntity(fit);
	}

	@Override
	public FitResponseDto conditionalSaveFit(FitResponseDto fitResponseDto) {

		log.info("Serving request to conditionally save a Fit with response object: {}", fitResponseDto);

		Fit fit = fitDbService.findByNameAndFactoryId(fitResponseDto.getName(), fitResponseDto.getFactoryId());

		if (Objects.nonNull(fit)) {
			log.error("Fit already exists with name: {} and factory_id: {}", fitResponseDto.getName(), fitResponseDto.getFactoryId());
			return null;
		}

		fit = FitAdapter.buildFitFromResponse(fitResponseDto, fitResponseDto.getFactoryId());

		fit = fitDbService.save(fit);

		fitResponseDto = FitAdapter.buildResponseFromEntity(fit);

		return fitResponseDto;
	}

	@Override
	public FitResponseDto activateDeactivateFit(String id, boolean status) {

		log.info("Serving request to activate / deactivate a fit with id:{}", id);

		Fit fit = fitDbService.getFitById(id);

		if (Objects.isNull(fit)) {
			String errorMsg = "Fit with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		fit = fitDbService.activateDeactivateFit(fit, status);

		return FitAdapter.buildResponseFromEntity(fit);
	}

	@Override
	public void saveEntityFromCache(Map<String, FitResponseDto> fitByNameMap) {

		List<String> factories = factoryHttpService.getFactoryIds();

		if (CollectionUtils.isEmpty(factories)) {
			log.error("No active factories found in the system to populate data for");
			return;
		}

		factories.forEach(factoryId -> {

			log.info("Populating Fit data for factory id: {}", factoryId);

			fitByNameMap.values().forEach(fitResponseDto -> {

				fitResponseDto.setFactoryId(factoryId);

				conditionalSaveFit(fitResponseDto);

			});
		});

	}

	@SuppressWarnings("unused")
	private void publishFit(FitResponseDto fitResponseDto, String type, String subType, String topicName) {

		KafkaDTO kafkaDTO = new KafkaDTO(type, subType, FitResponseDto.class.getName(), fitResponseDto);
		notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
	}

	@Override
	public void consumeFit(FitResponseDto fitResponseDto) {
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		Fit fit = FitAdapter.buildFitFromResponse(fitResponseDto, factoryId);

		if (Objects.isNull(fit)) {
			log.error("Unable to build fit from response object: {}", fitResponseDto);
			return;
		}

		fitDbService.saveFit(fit);
	}

	private boolean isEntityExistsWithName(String name) {

		return StringUtils.isNotBlank(name) && fitDbService.isEntityExistsByName(name);
	}

	private void runValidations(FitRequestDto fitRequestDto) {

		validateName(fitRequestDto);
	}

	private void validateName(FitRequestDto fitRequestDto) {

		if (isEntityExistsWithName(fitRequestDto.getName())) {
			String errorMsg = "Fit cannot be created/updated as record already exists with name: " + fitRequestDto.getName();
			throw new RecordExistsException(errorMsg);
		}
	}

	@Override
	public Fit findOrCreate(String name) {
		try {
			String factoryId = HeaderUtil.getFactoryIdHeaderValue();
			Fit fit = FitAdapter.buildFitFromName(name, factoryId);
			return fitDbService.findOrCreate(fit);
		} catch (Exception e) {
			throw new GroyyoException("Something went wrong!");
		}
	}
}
