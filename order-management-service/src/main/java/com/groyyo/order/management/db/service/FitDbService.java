package com.groyyo.order.management.db.service;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Fit;

import java.util.List;

public interface FitDbService extends AbstractJpaService<Fit, Long> {


    List<Fit> getAllFits(String factoryId);


    List<Fit> getAllFitsForStatus(boolean status,String factoryId);


    Fit getFitById(String id);


    Fit saveFit(Fit fit);


    Fit activateDeactivateFit(Fit fit, boolean status);


    boolean isEntityExistsByName(String name);

    Fit findOrCreate(Fit fit);
}
