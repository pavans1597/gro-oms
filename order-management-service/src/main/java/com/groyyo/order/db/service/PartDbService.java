package com.groyyo.order.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.entity.Part;

public interface PartDbService extends AbstractJpaService<Part, Long> {


    List<Part> getAllParts();


    List<Part> getAllPartsForStatus(boolean status);


    Part getPartById(String id);


    Part savePart(Part part);


    Part activateDeactivatePart(Part part, boolean status);


    boolean isEntityExistsByName(String name);
}
