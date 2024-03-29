package com.groyyo.order.management.db.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.SeasonDbService;
import com.groyyo.order.management.entity.Season;
import com.groyyo.order.management.repository.SeasonRepository;

@Service
public class SeasonDbServiceImpl extends AbstractJpaServiceImpl<Season, Long, SeasonRepository> implements SeasonDbService {

	@Autowired
	private SeasonRepository seasonRepository;

	@Override
	protected SeasonRepository getJpaRepository() {
		return seasonRepository;
	}

	@Override
	public List<Season> getAllSeasons(String factoryId) {
		return (!Objects.isNull(factoryId) ? seasonRepository.findByFactoryIdOrderByNameAsc(factoryId)
				: seasonRepository.findByOrderByNameAsc());
	}

	@Override
	public List<Season> getAllSeasonsForStatus(boolean status, String factoryId) {
		return (!Objects.isNull(factoryId) ? seasonRepository.findByStatusAndFactoryIdOrderByNameAsc(status, factoryId)
				: seasonRepository.findByStatusOrderByNameAsc(status));
	}

	@Override
	public Season getSeasonById(String id) {
		return seasonRepository.findByUuid(id);
	}

	@Override
	public Season saveSeason(Season season) {
		return seasonRepository.saveAndFlush(season);
	}

	@Override
	public Season activateDeactivateSeason(Season season, boolean status) {
		season.setStatus(status);
		return seasonRepository.saveAndFlush(season);
	}

	@Override
	public boolean isEntityExistsByName(String name) {
		return Objects.nonNull(seasonRepository.findByName(name));
	}

	@Override
	public Season findOrCreate(Season season) {
		Season entity = seasonRepository.findByNameAndFactoryId(season.getName(), season.getFactoryId());
		if (Objects.isNull(entity)) {
			entity = season;
			save(entity);
		}
		return entity;
	}

	@Override
	public Season findByNameAndFactoryId(String name, String factoryId) {
		return seasonRepository.findByNameAndFactoryId(name, factoryId);
	}
}
