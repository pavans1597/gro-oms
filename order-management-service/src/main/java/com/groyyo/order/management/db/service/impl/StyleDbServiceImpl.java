package com.groyyo.order.management.db.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.StyleDbService;
import com.groyyo.order.management.entity.Style;
import com.groyyo.order.management.repository.StyleRepository;

@Service
public class StyleDbServiceImpl extends AbstractJpaServiceImpl<Style, Long, StyleRepository> implements StyleDbService {

	@Autowired
	private StyleRepository styleRepository;

	@Override
	protected StyleRepository getJpaRepository() {
		return styleRepository;
	}

	@Override
	public List<Style> getAllStyles(String factoryId) {

		return (Objects.isNull(factoryId) ? styleRepository.findAll() : styleRepository.findAllByFactoryId(factoryId));
	}

	@Override
	public List<Style> getAllStylesForStatus(boolean status, String factoryId) {

		return Objects.nonNull(factoryId) ? styleRepository.findByStatusAndFactoryId(status, factoryId) : styleRepository.findByStatus(status);
	}

	@Override
	public Style getStyleById(String id) {
		return styleRepository.findByUuid(id);
	}

	@Override
	public Style saveStyle(Style style) {
		return styleRepository.saveAndFlush(style);
	}

	@Override
	public Style activateDeactivateStyle(Style style, boolean status) {
		style.setStatus(status);
		return styleRepository.saveAndFlush(style);
	}

	@Override
	public boolean isEntityExistsByStyleNumber(String styleNumber) {

		return Objects.nonNull(styleRepository.findByStyleNumber(styleNumber));

	}
}
