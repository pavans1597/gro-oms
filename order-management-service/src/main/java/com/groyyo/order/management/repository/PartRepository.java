package com.groyyo.order.management.repository;

import com.groyyo.order.management.entity.Part;
import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;

@Repository
public interface PartRepository extends AbstractJpaRepository<Part, Long> {

    Part findByName(String name);

    Part findByNameAndStatus(String name, Boolean status);

}
