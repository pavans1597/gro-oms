package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Season;

public interface SeasonDbService extends AbstractJpaService<Season, Long> {

	List<Season> getAllSeasons(String factoryId);

	List<Season> getAllSeasonsForStatus(boolean status, String factoryId);

	Season getSeasonById(String id);

	Season saveSeason(Season season);

	Season activateDeactivateSeason(Season season, boolean status);

	boolean isEntityExistsByName(String name);

	Season findOrCreate(Season season);

	/**
	 * @param name
	 * @param factoryId
	 * @return
	 */
	Season findByNameAndFactoryId(String name, String factoryId);
}
