package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Style;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleRepository extends AbstractJpaRepository<Style, Long> {

    Style findByName(String name);

    Style findByNameAndStatus(String name, Boolean status);

    Style findByStyleNumber(String styleNumber);


    Style findByNameAndFactoryId(String name,String factoryId);

    Style findByNameAndStatusAndFactoryId(String name, Boolean status,String factoryId);

    Style findByStyleNumberAndFactoryId(String styleNumber,String factoryId);

}