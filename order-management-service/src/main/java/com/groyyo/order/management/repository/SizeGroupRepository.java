package com.groyyo.order.management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.SizeGroup;

@Repository
public interface SizeGroupRepository extends AbstractJpaRepository<SizeGroup, Long> {

	List<SizeGroup> findByOrderByNameAsc();

	List<SizeGroup> findByStatusOrderByNameAsc(Boolean status);

	SizeGroup findByName(String name);

	SizeGroup findByNameAndStatus(String name, Boolean status);

}
