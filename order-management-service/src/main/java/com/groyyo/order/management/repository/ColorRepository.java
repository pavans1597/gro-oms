
package com.groyyo.order.management.repository;

import com.groyyo.core.sqlPostgresJpa.repository.AbstractJpaRepository;
import com.groyyo.order.management.entity.Color;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends AbstractJpaRepository<Color, Long> {

	List<Color> findByOrderByNameAsc();

	List<Color> findByStatusOrderByNameAsc(Boolean status);

	Color findByName(String name);

	Color findByNameAndStatus(String name, Boolean status);

	Color findByHexCode(String hexCode);

	Color findByHexCodeAndStatus(String hexCode, Boolean status);

	Color findByMasterId(String masterId);


	List<Color> findByStatusAndFactoryIdOrderByNameAsc(boolean status, String factoryId);

	List<Color> findByFactoryIdOrderByNameAsc(String factoryId);
}
