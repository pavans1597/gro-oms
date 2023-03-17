package com.groyyo.order.management.db.service;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Size;

import java.util.List;

public interface SizeDbService extends AbstractJpaService<Size, Long> {


    List<Size> getAllSizes(String factoryId);


    List<Size> getAllSizesForStatus(boolean status,String factoryId);


    Size getSizeById(String id);


    Size saveSize(Size size);


    Size activateDeactivateSize(Size size, boolean status);


    boolean isEntityExistsByName(String name);

    Size findOrCreate(Size size);
}
