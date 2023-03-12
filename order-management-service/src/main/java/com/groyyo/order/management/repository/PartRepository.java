package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Part;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends AbstractJpaRepository<Part, Long> {

	List<Part> findByOrderByNameAsc();

	List<Part> findByStatusOrderByNameAsc(Boolean status);

	Part findByName(String name);

	Part findByNameAndStatus(String name, Boolean status);

	List<Part> findByFactoryIdOrderByNameAsc(String factoryId);

	List<Part> findByStatusAndFactoryIdOrderByNameAsc(boolean status,String factoryId);
}
