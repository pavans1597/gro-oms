package com.groyyo.order.management.db.service.impl;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.FabricCategoryDbService;
import com.groyyo.order.management.entity.FabricCategory;
import com.groyyo.order.management.repository.FabricCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FabricCategoryDbServiceImpl extends AbstractJpaServiceImpl<FabricCategory, Long, FabricCategoryRepository> implements FabricCategoryDbService {

    @Autowired
    private FabricCategoryRepository fabricCategoryRepository;

    @Override
    protected FabricCategoryRepository getJpaRepository() {
        return fabricCategoryRepository;
    }

    @Override
    public List<FabricCategory> getAllFabricCategorys(String factoryId) {

        return (!Objects.isNull(factoryId)?
                fabricCategoryRepository.findAllByFactoryId(factoryId)
                : fabricCategoryRepository.findAll());
    }

    @Override
    public List<FabricCategory> getAllFabricCategorysForStatus(boolean status,String factoryId) {

        return (!Objects.isNull(factoryId)?
                fabricCategoryRepository.findByStatusAndFactoryId(status,factoryId)
                : fabricCategoryRepository.findByStatus(status));

    }

    @Override
    public FabricCategory getFabricCategoryById(String id) {
        return fabricCategoryRepository.findByUuid(id);
    }

    @Override
    public FabricCategory saveFabricCategory(FabricCategory fabricCategory) {
        return fabricCategoryRepository.saveAndFlush(fabricCategory);
    }

    @Override
    public FabricCategory activateDeactivateFabricCategory(FabricCategory fabricCategory, boolean status) {
        fabricCategory.setStatus(status);
        return fabricCategoryRepository.saveAndFlush(fabricCategory);
    }

    @Override
    public boolean isEntityExistsByName(String name) {
        return Objects.nonNull(fabricCategoryRepository.findByName(name));
    }

    @Override
    public boolean isEntityExistsByType(String type) {
        return Objects.nonNull(fabricCategoryRepository.findByType(type));
    }
}

