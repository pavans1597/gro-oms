package com.groyyo.order.management.repository;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Buyer;


@Repository
public interface BuyerRepository extends AbstractJpaRepository<Buyer, Long> {

    Buyer findByName(String name);

    Buyer findByNameAndStatus(String name, Boolean status);

}
