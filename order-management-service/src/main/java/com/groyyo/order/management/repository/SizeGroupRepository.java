package com.groyyo.order.management.repository;

import com.groyyo.order.management.entity.SizeGroup;
import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;


@Repository
public interface SizeGroupRepository extends AbstractJpaRepository<SizeGroup, Long> {

    SizeGroup findByName(String name);

    SizeGroup findByNameAndStatus(String name, Boolean status);

}
