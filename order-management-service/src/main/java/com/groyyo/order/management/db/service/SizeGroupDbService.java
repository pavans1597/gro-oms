package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.SizeGroup;

public interface SizeGroupDbService extends AbstractJpaService<SizeGroup, Long> {

	List<SizeGroup> getAllSizeGroups(String factoryId);

	List<SizeGroup> getAllSizeGroupsForStatus(boolean status, String factoryId);

	SizeGroup getSizeGroupById(String id);

	SizeGroup saveSizeGroup(SizeGroup sizeGroup);

	SizeGroup activateDeactivateSizeGroup(SizeGroup sizeGroup, boolean status);

	boolean isEntityExistsByName(String name);

	SizeGroup findOrCreate(SizeGroup sizeGroup);

	/**
	 * @param name
	 * @param factoryId
	 * @return
	 */
	SizeGroup findByNameAndFactoryId(String name, String factoryId);
}
