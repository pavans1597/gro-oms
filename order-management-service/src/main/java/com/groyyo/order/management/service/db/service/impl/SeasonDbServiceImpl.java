package com.groyyo.order.management.service.db.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.service.db.service.SeasonDbService;
import com.groyyo.order.management.service.entity.Season;
import com.groyyo.order.management.service.repository.SeasonRepository;


@Service
public class SeasonDbServiceImpl extends AbstractJpaServiceImpl<Season, Long, SeasonRepository> implements SeasonDbService {

    @Autowired
    private SeasonRepository seasonRepository;

    @Override
    protected SeasonRepository getJpaRepository() {
        return seasonRepository;
    }

    @Override
    public List<Season> getAllSeasons() {
        return seasonRepository.findAll();
    }

    @Override
    public List<Season> getAllSeasonsForStatus(boolean status) {
        return seasonRepository.findByStatus(status);
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
}
