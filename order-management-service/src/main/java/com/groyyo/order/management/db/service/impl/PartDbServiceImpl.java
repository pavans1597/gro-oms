package com.groyyo.order.management.db.service.impl;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.PartDbService;
import com.groyyo.order.management.entity.Part;
import com.groyyo.order.management.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PartDbServiceImpl extends AbstractJpaServiceImpl<Part, Long, PartRepository> implements PartDbService {

	@Autowired
	private PartRepository partRepository;

	@Override
	protected PartRepository getJpaRepository() {
		return partRepository;
	}

	@Override
	public List<Part> getAllParts(String factoryId) {
		return (!Objects.isNull(factoryId)?
				partRepository.findByFactoryIdOrderByNameAsc(factoryId)
		: partRepository.findByOrderByNameAsc());
	}

	@Override
	public List<Part> getAllPartsForStatus(boolean status,String factoryId) {
		return (!Objects.isNull(factoryId)?
				partRepository.findByStatusAndFactoryIdOrderByNameAsc(status,factoryId)
		: partRepository.findByStatusOrderByNameAsc(status));
	}

	@Override
	public Part getPartById(String id) {
		return partRepository.findByUuid(id);
	}

	@Override
	public Part savePart(Part part) {
		return partRepository.saveAndFlush(part);
	}

	@Override
	public Part activateDeactivatePart(Part part, boolean status) {
		part.setStatus(status);
		return partRepository.saveAndFlush(part);
	}

	@Override
	public boolean isEntityExistsByName(String name) {
		return Objects.nonNull(partRepository.findByName(name));
	}
}
