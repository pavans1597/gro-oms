package com.groyyo.order.repository;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.entity.SizeGroup;


@Repository
public interface SizeGroupRepository extends AbstractJpaRepository<SizeGroup, Long> {

    SizeGroup findByName(String name);

    SizeGroup findByNameAndStatus(String name, Boolean status);

}
