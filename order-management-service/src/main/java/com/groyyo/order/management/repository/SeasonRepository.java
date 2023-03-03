package com.groyyo.order.management.repository;

import com.groyyo.order.management.entity.Season;
import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;

@Repository
public interface SeasonRepository extends AbstractJpaRepository<Season, Long> {

    Season findByName(String name);

    Season findByNameAndStatus(String name, Boolean status);

}
