package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends AbstractJpaRepository<Product, Long> {

	List<Product> findByOrderByNameAsc();

	List<Product> findByStatusOrderByNameAsc(Boolean status);

	Product findByName(String name);

	Product findByNameAndStatus(String name, Boolean status);

	List<Product> findByFactoryIdOrderByNameAsc(String factoryId);

	List<Product> findByStatusAndFactoryIdOrderByNameAsc(boolean status, String factoryId);
}
