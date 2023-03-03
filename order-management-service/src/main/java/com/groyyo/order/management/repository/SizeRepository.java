package com.groyyo.order.management.repository;

import com.groyyo.order.management.entity.Size;
import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;


@Repository
public interface SizeRepository extends AbstractJpaRepository<Size, Long> {

    Size findByName(String name);

    Size findByNameAndStatus(String name, Boolean status);

}
