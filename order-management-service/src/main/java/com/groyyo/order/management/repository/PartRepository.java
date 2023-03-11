package com.groyyo.order.management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Part;

@Repository
public interface PartRepository extends AbstractJpaRepository<Part, Long> {

	List<Part> findByOrderByNameAsc();

	List<Part> findByStatusOrderByNameAsc(Boolean status);

	Part findByName(String name);

	Part findByNameAndStatus(String name, Boolean status);

}
