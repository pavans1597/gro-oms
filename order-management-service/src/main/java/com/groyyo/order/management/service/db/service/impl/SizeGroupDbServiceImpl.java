package com.groyyo.order.management.service.db.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.service.db.service.SizeGroupDbService;
import com.groyyo.order.management.service.entity.SizeGroup;
import com.groyyo.order.management.service.repository.SizeGroupRepository;


@Service
public class SizeGroupDbServiceImpl extends AbstractJpaServiceImpl<SizeGroup, Long, SizeGroupRepository> implements SizeGroupDbService {

    @Autowired
    private SizeGroupRepository sizeGroupRepository;

    @Override
    protected SizeGroupRepository getJpaRepository() {
        return sizeGroupRepository;
    }

    @Override
    public List<SizeGroup> getAllSizeGroups() {
        return sizeGroupRepository.findAll();
    }

    @Override
    public List<SizeGroup> getAllSizeGroupsForStatus(boolean status) {
        return sizeGroupRepository.findByStatus(status);
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

    public static class PurchaseOrderQuantityDbService {
    }
}
