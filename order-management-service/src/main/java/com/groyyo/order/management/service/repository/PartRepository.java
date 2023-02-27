package com.groyyo.order.management.service.repository;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.service.entity.Part;

@Repository
public interface PartRepository extends AbstractJpaRepository<Part, Long> {

    Part findByName(String name);

    Part findByNameAndStatus(String name, Boolean status);

}
