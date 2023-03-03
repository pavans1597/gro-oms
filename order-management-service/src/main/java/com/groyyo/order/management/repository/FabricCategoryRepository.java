package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.FabricCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface FabricCategoryRepository extends AbstractJpaRepository<FabricCategory, Long> {

    FabricCategory findByName(String name);

    FabricCategory findByNameAndStatus(String name, Boolean status);

    FabricCategory findByType(String type);

    FabricCategory findByTypeAndStatus(String type, Boolean status);

}