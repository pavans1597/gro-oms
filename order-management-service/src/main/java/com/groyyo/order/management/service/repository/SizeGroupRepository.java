package com.groyyo.order.management.service.repository;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.service.entity.SizeGroup;


@Repository
public interface SizeGroupRepository extends AbstractJpaRepository<SizeGroup, Long> {

    SizeGroup findByName(String name);

    SizeGroup findByNameAndStatus(String name, Boolean status);

}
