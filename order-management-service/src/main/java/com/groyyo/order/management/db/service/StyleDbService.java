package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Style;

public interface StyleDbService extends AbstractJpaService<Style, Long> {

	List<Style> getAllStyles(String factoryId);

	List<Style> getAllStylesForStatus(boolean status, String factoryId);

	Style getStyleById(String id);

	Style saveStyle(Style style);

	Style activateDeactivateStyle(Style style, boolean status);

	boolean isEntityExistsByStyleNumber(String styleNumber);

	Style findOrCreate(Style style);

	/**
	 * @param styleNumber
	 * @param factoryId
	 * @return
	 */
	boolean isEntityExistsByStyleNumberAndFactoryId(String styleNumber, String factoryId);
}
