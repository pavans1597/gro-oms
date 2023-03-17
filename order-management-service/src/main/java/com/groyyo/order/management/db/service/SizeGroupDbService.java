package com.groyyo.order.management.db.service;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.SizeGroup;

import java.util.List;

public interface SizeGroupDbService extends AbstractJpaService<SizeGroup, Long> {


    List<SizeGroup> getAllSizeGroups(String factoryId);


    List<SizeGroup> getAllSizeGroupsForStatus(boolean status,String factoryId);


    SizeGroup getSizeGroupById(String id);


    SizeGroup saveSizeGroup(SizeGroup sizeGroup);


    SizeGroup activateDeactivateSizeGroup(SizeGroup sizeGroup, boolean status);


    boolean isEntityExistsByName(String name);

    SizeGroup findOrCreate(SizeGroup sizeGroup);
}
