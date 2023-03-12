package com.groyyo.order.management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.FabricCategory;

@Repository
public interface FabricCategoryRepository extends AbstractJpaRepository<FabricCategory, Long> {

	List<FabricCategory> findAllByFactoryId(String factoryId);

	List<FabricCategory> findByStatusAndFactoryId(Boolean status, String factoryId);

	FabricCategory findByName(String name);

	FabricCategory findByNameAndStatus(String name, Boolean status);

	FabricCategory findByType(String type);

	FabricCategory findByTypeAndStatus(String type, Boolean status);

}
