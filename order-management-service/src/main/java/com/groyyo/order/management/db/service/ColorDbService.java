package com.groyyo.order.management.db.service;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Color;

import java.util.List;

public interface ColorDbService extends AbstractJpaService<Color, Long> {


    List<Color> getAllColors(String factoryId);


    List<Color> getAllColorsForStatus(boolean status,String factoryId);


    Color getColorById(String id);


    Color saveColor(Color color);


    Color activateDeactivateColor(Color color, boolean status);


    boolean isEntityExistsByName(String name);


    boolean isEntityExistsByHexCode(String hexCode);

    Color findOrCreate(Color color);
}
