package com.groyyo.order.management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Product;

@Repository
public interface ProductRepository extends AbstractJpaRepository<Product, Long> {

	List<Product> findByOrderByNameAsc();

	List<Product> findByStatusOrderByNameAsc(Boolean status);

	Product findByName(String name);

	Product findByNameAndStatus(String name, Boolean status);

}
