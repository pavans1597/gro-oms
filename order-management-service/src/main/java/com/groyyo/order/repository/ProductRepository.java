package com.groyyo.order.repository;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.entity.Product;


@Repository
public interface ProductRepository extends AbstractJpaRepository<Product, Long> {

    Product findByName(String name);

    Product findByNameAndStatus(String name, Boolean status);

}
