package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Buyer;
import org.springframework.stereotype.Repository;


@Repository
public interface BuyerRepository extends AbstractJpaRepository<Buyer, Long> {

    Buyer findByName(String name);

    Buyer findByNameAndStatus(String name, Boolean status);



}
