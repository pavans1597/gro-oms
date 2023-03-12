package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Season;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends AbstractJpaRepository<Season, Long> {

	List<Season> findByOrderByNameAsc();

	List<Season> findByStatusOrderByNameAsc(Boolean status);

	Season findByName(String name);

	Season findByNameAndStatus(String name, Boolean status);

	List<Season> findByFactoryIdOrderByNameAsc(String factoryId);

	List<Season> findByStatusAndFactoryIdOrderByNameAsc(boolean status, String factoryId);
}
