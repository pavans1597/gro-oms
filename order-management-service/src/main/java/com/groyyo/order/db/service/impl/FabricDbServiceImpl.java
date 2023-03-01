package com.groyyo.order.db.service.impl;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.db.service.FabricDbService;
import com.groyyo.order.entity.Fabric;
import com.groyyo.order.repository.FabricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FabricDbServiceImpl extends AbstractJpaServiceImpl<Fabric, Long, FabricRepository> implements FabricDbService {

    @Autowired
    private FabricRepository fabricRepository;

    @Override
    protected FabricRepository getJpaRepository() {
        return fabricRepository;
    }

    @Override
    public List<Fabric> getAllFabrics() {
        return fabricRepository.findAll();
    }

    @Override
    public List<Fabric> getAllFabricsForStatus(boolean status) {
        return fabricRepository.findByStatus(status);
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

