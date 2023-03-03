package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Size;

public interface SizeDbService extends AbstractJpaService<Size, Long> {


    List<Size> getAllSizes();


    List<Size> getAllSizesForStatus(boolean status);


    Size getSizeById(String id);


    Size saveSize(Size size);


    Size activateDeactivateSize(Size size, boolean status);


    boolean isEntityExistsByName(String name);
}
