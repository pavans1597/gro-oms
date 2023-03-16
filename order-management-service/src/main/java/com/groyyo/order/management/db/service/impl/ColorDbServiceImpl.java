package com.groyyo.order.management.db.service.impl;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.ColorDbService;
import com.groyyo.order.management.entity.Color;
import com.groyyo.order.management.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ColorDbServiceImpl extends AbstractJpaServiceImpl<Color, Long, ColorRepository> implements ColorDbService {

	@Autowired
	private ColorRepository colorRepository;

	@Override
	protected ColorRepository getJpaRepository() {
		return colorRepository;
	}

	@Override
	public List<Color> getAllColors(String factoryId) {
		return (!Objects.isNull(factoryId)?
				colorRepository.findByFactoryIdOrderByNameAsc(factoryId)
				: colorRepository.findByOrderByNameAsc());
	}

	@Override
	public List<Color> getAllColorsForStatus(boolean status,String factoryId) {
		return  (!Objects.isNull(factoryId))? colorRepository.findByStatusAndFactoryIdOrderByNameAsc(status,factoryId) :
				colorRepository.findByStatusOrderByNameAsc(status);
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

	@Override
	public Color findOrCreate(Color color) {
		Color entity = colorRepository.findByName(color.getName());
		if (entity == null) {
			entity = color;
			save(entity);
		}
		return entity;
	}
}
