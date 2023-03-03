package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Fabric;
import org.springframework.stereotype.Repository;

@Repository
public interface FabricRepository extends AbstractJpaRepository<Fabric, Long> {

    Fabric findByName(String name);

    Fabric findByNameAndStatus(String name, Boolean status);

    Fabric findByFabricCategory(String fabricCategory);

    Fabric findByFabricCategoryAndStatus(String fabricCategory, Boolean status);

}