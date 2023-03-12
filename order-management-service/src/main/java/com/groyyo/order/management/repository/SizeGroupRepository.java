package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.SizeGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeGroupRepository extends AbstractJpaRepository<SizeGroup, Long> {

	List<SizeGroup> findByOrderByNameAsc();

	List<SizeGroup> findByStatusOrderByNameAsc(Boolean status);

	SizeGroup findByName(String name);

	SizeGroup findByNameAndStatus(String name, Boolean status);

	List<SizeGroup> findByFactoryIdOrderByNameAsc(String factoryId);

	List<SizeGroup> findByStatusAndFactoryIdOrderByNameAsc(boolean status, String factoryId);
}
