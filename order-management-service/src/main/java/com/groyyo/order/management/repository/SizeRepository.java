package com.groyyo.order.management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Size;

@Repository
public interface SizeRepository extends AbstractJpaRepository<Size, Long> {

	List<Size> findByOrderByNameAsc();

	List<Size> findByStatusOrderByNameAsc(Boolean status);

	Size findByName(String name);

	Size findByNameAndStatus(String name, Boolean status);

	List<Size> findByFactoryIdOrderByNameAsc(String factoryId);

	List<Size> findByStatusAndFactoryIdOrderByNameAsc(boolean status, String factoryId);

	Size findByNameAndFactoryId(String name, String factoryId);
}
