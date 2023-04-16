package com.groyyo.order.management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Buyer;

@Repository
public interface BuyerRepository extends AbstractJpaRepository<Buyer, Long> {

	List<Buyer> findAllByFactoryId(String factoryId);

	List<Buyer> findByStatusAndFactoryId(Boolean status, String factoryId);

	Buyer findByName(String name);

	Buyer findByNameAndStatus(String name, Boolean status);

	Buyer findByNameAndFactoryId(String name, String factoryId);

}
