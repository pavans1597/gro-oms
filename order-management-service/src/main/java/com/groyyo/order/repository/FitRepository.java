package com.groyyo.order.repository;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.entity.Fit;

@Repository
public interface FitRepository extends AbstractJpaRepository<Fit, Long> {

    Fit findByName(String name);

    Fit findByNameAndStatus(String name, Boolean status);

}
