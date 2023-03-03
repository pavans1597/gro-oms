package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Color;

public interface ColorDbService extends AbstractJpaService<Color, Long> {


    List<Color> getAllColors();


    List<Color> getAllColorsForStatus(boolean status);


    Color getColorById(String id);


    Color saveColor(Color color);


    Color activateDeactivateColor(Color color, boolean status);


    boolean isEntityExistsByName(String name);


    boolean isEntityExistsByHexCode(String hexCode);
}
