package com.groyyo.order.management.service.db.service;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.service.entity.FabricCategory;

import java.util.List;

public interface FabricCategoryDbService extends AbstractJpaService<FabricCategory, Long> {


    List<FabricCategory> getAllFabricCategorys();


    List<FabricCategory> getAllFabricCategorysForStatus(boolean status);


    FabricCategory getFabricCategoryById(String id);


    FabricCategory saveFabricCategory(FabricCategory fabricCategory);


    FabricCategory activateDeactivateFabricCategory(FabricCategory fabricCategory, boolean status);


    boolean isEntityExistsByName(String name);


    boolean isEntityExistsByType(String type);
}
