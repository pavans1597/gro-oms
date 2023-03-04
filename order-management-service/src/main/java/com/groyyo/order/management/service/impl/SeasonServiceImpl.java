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
import com.groyyo.core.master.dto.request.SeasonRequestDto;
import com.groyyo.core.master.dto.response.SeasonResponseDto;
import com.groyyo.order.management.adapter.SeasonAdapter;
import com.groyyo.order.management.constants.KafkaConstants;
import com.groyyo.order.management.db.service.SeasonDbService;
import com.groyyo.order.management.entity.Season;
import com.groyyo.order.management.service.SeasonService;

import lombok.extern.log4j.Log4j2;

/**
 * @author nipunaggarwal
 *
 */
@Service
@Log4j2
public class SeasonServiceImpl implements SeasonService {

	@Value("${kafka.master.updates.topic}")
	private String kafkaMasterDataUpdatesTopic;

	@Autowired
	private NotificationProducer notificationProducer;

	@Autowired
	private SeasonDbService seasonDbService;

	@Override
	public List<SeasonResponseDto> getAllSeasons(Boolean status) {

		log.info("Serving request to get all seasons");

		List<Season> seasonEntities = Objects.isNull(status) ? seasonDbService.getAllSeasons()
				: seasonDbService.getAllSeasonsForStatus(status);

		if (CollectionUtils.isEmpty(seasonEntities)) {
			log.error("No Seasons found in the system");
			return new ArrayList<SeasonResponseDto>();
		}

		return SeasonAdapter.buildResponsesFromEntities(seasonEntities);
	}

	@Override
	public SeasonResponseDto getSeasonById(String id) {

		log.info("Serving request to get a season by id:{}", id);

		Season season = seasonDbService.getSeasonById(id);

		if (Objects.isNull(season)) {
			String errorMsg = "Size with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		return SeasonAdapter.buildResponseFromEntity(season);
	}

	@Override
	public SeasonResponseDto addSeason(SeasonRequestDto seasonRequestDto) {

		log.info("Serving request to add a season with request object:{}", seasonRequestDto);

		runValidations(seasonRequestDto);

		Season season = SeasonAdapter.buildSeasonFromRequest(seasonRequestDto);

		season = seasonDbService.saveSeason(season);

		if (Objects.isNull(season)) {
			log.error("Unable to add season for object: {}", seasonRequestDto);
			return null;
		}

		SeasonResponseDto seasonResponseDto = SeasonAdapter.buildResponseFromEntity(season);

		// publishSeason(seasonResponseDto, KafkaConstants.KAFKA_SEASON_TYPE,
		// KafkaConstants.KAFKA_SEASON_SUBTYPE_CREATE, kafkaMasterDataUpdatesTopic);

		return seasonResponseDto;
	}

	@Override
	public SeasonResponseDto updateSeason(SeasonRequestDto seasonRequestDto) {

		log.info("Serving request to update a season with request object:{}", seasonRequestDto);

		Season season = seasonDbService.getSeasonById(seasonRequestDto.getId());

		if (Objects.isNull(season)) {
			log.error("Season with id: {} not found in the system", seasonRequestDto.getId());
			return null;
		}

		runValidations(seasonRequestDto);

		season = SeasonAdapter.cloneSeasonWithRequest(seasonRequestDto, season);

		seasonDbService.saveSeason(season);

		SeasonResponseDto seasonResponseDto = SeasonAdapter.buildResponseFromEntity(season);

		publishSeason(seasonResponseDto, KafkaConstants.KAFKA_SEASON_TYPE, KafkaConstants.KAFKA_SEASON_SUBTYPE_UPDATE, kafkaMasterDataUpdatesTopic);

		return seasonResponseDto;
	}

	@Override
	public SeasonResponseDto activateDeactivateSeason(String id, boolean status) {

		log.info("Serving request to activate / deactivate a season with id:{}", id);

		Season season = seasonDbService.getSeasonById(id);

		if (Objects.isNull(season)) {
			String errorMsg = "Size with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		season = seasonDbService.activateDeactivateSeason(season, status);

		return SeasonAdapter.buildResponseFromEntity(season);
	}

	private void publishSeason(SeasonResponseDto seasonResponseDto, String type, String subType, String topicName) {

		KafkaDTO kafkaDTO = new KafkaDTO(type, subType, SeasonResponseDto.class.getName(), seasonResponseDto);
		notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
	}

	@Override
	public void consumeSeason(SeasonResponseDto seasonResponseDto) {
		Season season = SeasonAdapter.buildSeasonFromResponse(seasonResponseDto);

		if (Objects.isNull(season)) {
			log.error("Unable to build season from response object: {}", seasonResponseDto);
			return;
		}

		seasonDbService.saveSeason(season);
	}

	@Override
	public void saveEntityFromCache(Map<String, SeasonResponseDto> seasonByNameMap) {

		seasonByNameMap.values().forEach(seasonResponseDto -> {

			SeasonRequestDto seasonRequestDto = SeasonAdapter.buildRequestFromResponse(seasonResponseDto);

			if (Objects.nonNull(seasonRequestDto)) {

				try {

					addSeason(seasonRequestDto);

				} catch (Exception e) {

					log.error("Exception caught while saving season entity with data: {} from cache", seasonByNameMap, e);
				}
			}
		});

	}

	private boolean isEntityExistsWithName(String name) {

		return StringUtils.isNotBlank(name) && seasonDbService.isEntityExistsByName(name);
	}

	private void runValidations(SeasonRequestDto seasonRequestDto) {

		validateName(seasonRequestDto);
	}

	private void validateName(SeasonRequestDto seasonRequestDto) {

		if (isEntityExistsWithName(seasonRequestDto.getName())) {
			String errorMsg = "Season cannot be created/updated as record already exists with name: " + seasonRequestDto.getName();
			throw new RecordExistsException(errorMsg);
		}
	}
}
