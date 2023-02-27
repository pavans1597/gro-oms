package com.groyyo.order.management.service.repository;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.service.entity.Fit;

@Repository
public interface FitRepository extends AbstractJpaRepository<Fit, Long> {

    Fit findByName(String name);

    Fit findByNameAndStatus(String name, Boolean status);

}
