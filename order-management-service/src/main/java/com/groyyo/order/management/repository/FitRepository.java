package com.groyyo.order.management.repository;

import com.groyyo.order.management.entity.Fit;
import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;

@Repository
public interface FitRepository extends AbstractJpaRepository<Fit, Long> {

    Fit findByName(String name);

    Fit findByNameAndStatus(String name, Boolean status);

}
