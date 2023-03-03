package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Fit;

public interface FitDbService extends AbstractJpaService<Fit, Long> {


    List<Fit> getAllFits();


    List<Fit> getAllFitsForStatus(boolean status);


    Fit getFitById(String id);


    Fit saveFit(Fit fit);


    Fit activateDeactivateFit(Fit fit, boolean status);


    boolean isEntityExistsByName(String name);
}
