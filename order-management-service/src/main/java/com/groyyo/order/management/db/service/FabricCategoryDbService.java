package com.groyyo.order.management.db.service;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.FabricCategory;

import java.util.List;

public interface FabricCategoryDbService extends AbstractJpaService<FabricCategory, Long> {


    List<FabricCategory> getAllFabricCategorys(String factoryId);


    List<FabricCategory> getAllFabricCategorysForStatus(boolean status,String factoryId);


    FabricCategory getFabricCategoryById(String id);


    FabricCategory saveFabricCategory(FabricCategory fabricCategory);


    FabricCategory activateDeactivateFabricCategory(FabricCategory fabricCategory, boolean status);


    boolean isEntityExistsByName(String name);


    boolean isEntityExistsByType(String type);
}
