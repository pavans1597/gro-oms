
package com.groyyo.order.management.service.repository;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.service.entity.Color;

@Repository
public interface ColorRepository extends AbstractJpaRepository<Color, Long> {

    Color findByName(String name);

    Color findByNameAndStatus(String name, Boolean status);

    Color findByHexCode(String hexCode);

    Color findByHexCodeAndStatus(String hexCode, Boolean status);

}
