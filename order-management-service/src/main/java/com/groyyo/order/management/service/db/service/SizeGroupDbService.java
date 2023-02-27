package com.groyyo.order.management.service.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.service.entity.SizeGroup;

public interface SizeGroupDbService extends AbstractJpaService<SizeGroup, Long> {


    List<SizeGroup> getAllSizeGroups();


    List<SizeGroup> getAllSizeGroupsForStatus(boolean status);


    SizeGroup getSizeGroupById(String id);


    SizeGroup saveSizeGroup(SizeGroup sizeGroup);


    SizeGroup activateDeactivateSizeGroup(SizeGroup sizeGroup, boolean status);


    boolean isEntityExistsByName(String name);
}
