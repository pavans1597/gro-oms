package com.groyyo.order.management.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Season;

@Repository
public interface SeasonRepository extends AbstractJpaRepository<Season, Long> {

	List<Season> findByOrderByNameAsc();

	List<Season> findByStatusOrderByNameAsc(Boolean status);

	Season findByName(String name);

	Season findByNameAndStatus(String name, Boolean status);

}
