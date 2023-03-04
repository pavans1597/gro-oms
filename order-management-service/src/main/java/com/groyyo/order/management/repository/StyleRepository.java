package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Style;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleRepository extends AbstractJpaRepository<Style, Long> {

    Style findByName(String name);

    Style findByNameAndStatus(String name, Boolean status);

    Style findByStyleNumber(String styleNumber);

}