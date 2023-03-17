package com.groyyo.order.management.db.service;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Part;

import java.util.List;

public interface PartDbService extends AbstractJpaService<Part, Long> {


    List<Part> getAllParts(String factoryId);


    List<Part> getAllPartsForStatus(boolean status,String factoryId);


    Part getPartById(String id);


    Part savePart(Part part);


    Part activateDeactivatePart(Part part, boolean status);


    boolean isEntityExistsByName(String name);

    Part findOrCreate(Part part);
}
