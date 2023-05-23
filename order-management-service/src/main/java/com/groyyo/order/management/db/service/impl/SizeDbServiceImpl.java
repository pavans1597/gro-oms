package com.groyyo.order.management.db.service.impl;

import java.util.List;
import java.util.Objects;

import com.groyyo.core.base.exception.GroyyoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.SizeDbService;
import com.groyyo.order.management.entity.Size;
import com.groyyo.order.management.repository.SizeRepository;

@Service
public class SizeDbServiceImpl extends AbstractJpaServiceImpl<Size, Long, SizeRepository> implements SizeDbService {

	@Autowired
	private SizeRepository sizeRepository;

	@Override
	protected SizeRepository getJpaRepository() {
		return sizeRepository;
	}

	@Override
	public List<Size> getAllSizes(String factoryId) {
		return Objects.isNull(factoryId) ? sizeRepository.findByOrderByNameAsc()
				: sizeRepository.findByFactoryIdOrderByNameAsc(factoryId);
	}

	@Override
	public List<Size> getAllSizesForStatus(boolean status, String factoryId) {
		return Objects.isNull(factoryId) ? sizeRepository.findByStatusOrderByNameAsc(status)
				: sizeRepository.findByStatusAndFactoryIdOrderByNameAsc(status, factoryId);
	}

	@Override
	public Size getSizeById(String id) {
		return sizeRepository.findByUuid(id);
	}

	@Override
	public Size saveSize(Size size) {
		return sizeRepository.saveAndFlush(size);
	}

	@Override
	public Size activateDeactivateSize(Size size, boolean status) {
		size.setStatus(status);
		return sizeRepository.saveAndFlush(size);
	}

	@Override
	public boolean isEntityExistsByName(String name) {
		return Objects.nonNull(sizeRepository.findByName(name));
	}

	@Override
	public Size findOrCreate(Size size) {
		try {
			Size entity = sizeRepository.findByNameAndFactoryId(size.getName(), size.getFactoryId());
			if (Objects.isNull(entity)) {
				entity = size;
				save(entity);
			}
			return entity;
		} catch (Exception e) {
			throw new GroyyoException("Something went wrong!");
		}
	}

	@Override
	public Size findByNameAndFactoryId(String name, String factoryId) {
		return sizeRepository.findByNameAndFactoryId(name, factoryId);
	}
}
