package com.groyyo.order.management.repository;

import com.groyyo.order.management.entity.Product;
import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;


@Repository
public interface ProductRepository extends AbstractJpaRepository<Product, Long> {

    Product findByName(String name);

    Product findByNameAndStatus(String name, Boolean status);

}
