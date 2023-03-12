package com.groyyo.order.management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Fabric;

@Repository
public interface FabricRepository extends AbstractJpaRepository<Fabric, Long> {

	List<Fabric> findAllByFactoryId(String factoryId);

	List<Fabric> findByStatusAndFactoryId(Boolean status, String factoryId);

	Fabric findByName(String name);

	Fabric findByNameAndStatus(String name, Boolean status);

	Fabric findByFabricCategory(String fabricCategory);

	Fabric findByFabricCategoryAndStatus(String fabricCategory, Boolean status);

}
