package com.groyyo.order.management.service.repository;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.service.entity.Size;


@Repository
public interface SizeRepository extends AbstractJpaRepository<Size, Long> {

    Size findByName(String name);

    Size findByNameAndStatus(String name, Boolean status);

}
