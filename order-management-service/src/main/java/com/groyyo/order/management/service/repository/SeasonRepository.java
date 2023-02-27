package com.groyyo.order.management.service.repository;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.service.entity.Season;

@Repository
public interface SeasonRepository extends AbstractJpaRepository<Season, Long> {

    Season findByName(String name);

    Season findByNameAndStatus(String name, Boolean status);

}
