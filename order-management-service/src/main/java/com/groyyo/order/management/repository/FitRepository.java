package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Fit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FitRepository extends AbstractJpaRepository<Fit, Long> {

	List<Fit> findByOrderByNameAsc();

	List<Fit> findByStatusOrderByNameAsc(Boolean status);

	Fit findByName(String name);

	Fit findByNameAndStatus(String name, Boolean status);

	List<Fit> findByAndFactoryIdOrderByNameAsc(String factoryId);

	List<Fit> findByStatusAndFactoryIdOrderByNameAsc(boolean status, String factoryId);
}
