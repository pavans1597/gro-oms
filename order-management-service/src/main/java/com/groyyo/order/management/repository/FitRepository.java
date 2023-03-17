package com.groyyo.order.management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Fit;

@Repository
public interface FitRepository extends AbstractJpaRepository<Fit, Long> {

	List<Fit> findByOrderByNameAsc();

	List<Fit> findByStatusOrderByNameAsc(Boolean status);

	Fit findByName(String name);

	Fit findByNameAndStatus(String name, Boolean status);

	List<Fit> findByAndFactoryIdOrderByNameAsc(String factoryId);

	List<Fit> findByStatusAndFactoryIdOrderByNameAsc(boolean status, String factoryId);

	Fit findByNameAndFactoryId(String name, String factoryId);
}
