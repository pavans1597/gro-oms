package com.groyyo.order.management.service.db.service;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.service.entity.Fabric;

import java.util.List;

public interface FabricDbService extends AbstractJpaService<Fabric, Long> {


    List<Fabric> getAllFabrics();


    List<Fabric> getAllFabricsForStatus(boolean status);


    Fabric getFabricById(String id);


    Fabric saveFabric(Fabric fabric);


    Fabric activateDeactivateFabric(Fabric fabric, boolean status);


    boolean isEntityExistsByName(String name);


    boolean isEntityExistsByFabricCategory(String fabricCategory);
}

