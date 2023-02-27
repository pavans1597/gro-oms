package com.groyyo.order.management.service.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.service.entity.Season;

public interface SeasonDbService extends AbstractJpaService<Season, Long> {


    List<Season> getAllSeasons();


    List<Season> getAllSeasonsForStatus(boolean status);


    Season getSeasonById(String id);


    Season saveSeason(Season season);


    Season activateDeactivateSeason(Season season, boolean status);


    boolean isEntityExistsByName(String name);
}
