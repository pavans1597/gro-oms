package com.groyyo.order.management.db.service.impl;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.SizeGroupDbService;
import com.groyyo.order.management.entity.SizeGroup;
import com.groyyo.order.management.repository.SizeGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class SizeGroupDbServiceImpl extends AbstractJpaServiceImpl<SizeGroup, Long, SizeGroupRepository> implements SizeGroupDbService {

    @Autowired
    private SizeGroupRepository sizeGroupRepository;

    @Override
    protected SizeGroupRepository getJpaRepository() {
        return sizeGroupRepository;
    }

    @Override
    public List<SizeGroup> getAllSizeGroups(String factoryId) {
        return (!Objects.isNull(factoryId) ?
                sizeGroupRepository.findByFactoryIdOrderByNameAsc(factoryId)
                : sizeGroupRepository.findByOrderByNameAsc());
    }

    @Override
    public List<SizeGroup> getAllSizeGroupsForStatus(boolean status, String factoryId) {
        return (!Objects.isNull(factoryId) ?
                sizeGroupRepository.findByStatusAndFactoryIdOrderByNameAsc(status, factoryId)
                : sizeGroupRepository.findByStatusOrderByNameAsc(status));
    }

    @Override
    public SizeGroup getSizeGroupById(String id) {
        return sizeGroupRepository.findByUuid(id);
    }

    @Override
    public SizeGroup saveSizeGroup(SizeGroup sizeGroup) {
        return sizeGroupRepository.saveAndFlush(sizeGroup);
    }

    @Override
    public SizeGroup activateDeactivateSizeGroup(SizeGroup sizeGroup, boolean status) {
        sizeGroup.setStatus(status);
        return sizeGroupRepository.saveAndFlush(sizeGroup);
    }

    @Override
    public boolean isEntityExistsByName(String name) {
        return Objects.nonNull(sizeGroupRepository.findByName(name));
    }

    @Override
    public SizeGroup findOrCreate(SizeGroup sizeGroup) {
        SizeGroup entity = sizeGroupRepository.findByName(sizeGroup.getName());
        if (entity == null) {
            entity = sizeGroup;
            save(entity);
        }
        return entity;
    }
}
