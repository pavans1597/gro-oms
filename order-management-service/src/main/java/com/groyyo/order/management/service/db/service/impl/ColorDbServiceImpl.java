package com.groyyo.order.management.service.db.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.service.db.service.ColorDbService;
import com.groyyo.order.management.service.entity.Color;
import com.groyyo.order.management.service.repository.ColorRepository;


@Service
public class ColorDbServiceImpl extends AbstractJpaServiceImpl<Color, Long, ColorRepository> implements ColorDbService {

	@Autowired
	private ColorRepository colorRepository;

	@Override
	protected ColorRepository getJpaRepository() {
		return colorRepository;
	}

	@Override
	public List<Color> getAllColors() {
		return colorRepository.findAll();
	}

	@Override
	public List<Color> getAllColorsForStatus(boolean status) {
		return colorRepository.findByStatus(status);
	}

	@Override
	public Color getColorById(String id) {
		return colorRepository.findByUuid(id);
	}

	@Override
	public Color saveColor(Color color) {
		return colorRepository.saveAndFlush(color);
	}

	@Override
	public Color activateDeactivateColor(Color color, boolean status) {
		color.setStatus(status);
		return colorRepository.saveAndFlush(color);
	}

	@Override
	public boolean isEntityExistsByName(String name) {
		return Objects.nonNull(colorRepository.findByName(name));
	}

	@Override
	public boolean isEntityExistsByHexCode(String hexCode) {
		return Objects.nonNull(colorRepository.findByHexCode(hexCode));
	}
}
