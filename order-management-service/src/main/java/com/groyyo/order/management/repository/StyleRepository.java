package com.groyyo.order.management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Style;

@Repository
public interface StyleRepository extends AbstractJpaRepository<Style, Long> {

	List<Style> findAllByFactoryId(String factoryId);

	List<Style> findByStatusAndFactoryId(Boolean status, String factoryId);

	Style findByName(String name);

	Style findByNameAndStatus(String name, Boolean status);

	Style findByStyleNumber(String styleNumber);

	Style findByNameAndFactoryId(String name, String factoryId);

	Style findByNameAndStatusAndFactoryId(String name, Boolean status, String factoryId);

	Style findByStyleNumberAndFactoryId(String styleNumber, String factoryId);

}