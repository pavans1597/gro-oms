package com.groyyo.order.management.db.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.FabricDbService;
import com.groyyo.order.management.entity.Fabric;
import com.groyyo.order.management.repository.FabricRepository;

@Service
public class FabricDbServiceImpl extends AbstractJpaServiceImpl<Fabric, Long, FabricRepository> implements FabricDbService {

	@Autowired
	private FabricRepository fabricRepository;

	@Override
	protected FabricRepository getJpaRepository() {
		return fabricRepository;
	}

	@Override
	public List<Fabric> getAllFabrics(String factoryId) {
		return (Objects.nonNull(factoryId) ? fabricRepository.findAllByFactoryId(factoryId) : fabricRepository.findAll());
	}

	@Override
	public List<Fabric> getAllFabricsForStatus(boolean status, String factoryId) {
		return (Objects.nonNull(factoryId) ? fabricRepository.findByStatusAndFactoryId(status, factoryId) : fabricRepository.findByStatus(status));
	}

	@Override
	public Fabric getFabricById(String id) {
		return fabricRepository.findByUuid(id);
	}

	@Override
	public Fabric saveFabric(Fabric fabric) {
		return fabricRepository.saveAndFlush(fabric);
	}

	@Override
	public Fabric activateDeactivateFabric(Fabric fabric, boolean status) {
		fabric.setStatus(status);
		return fabricRepository.saveAndFlush(fabric);
	}

	@Override
	public boolean isEntityExistsByName(String name) {
		return Objects.nonNull(fabricRepository.findByName(name));
	}

	@Override
	public boolean isEntityExistsByFabricCategory(String fabricCategory) {
		return Objects.nonNull(fabricRepository.findByFabricCategory(fabricCategory));
	}
}
