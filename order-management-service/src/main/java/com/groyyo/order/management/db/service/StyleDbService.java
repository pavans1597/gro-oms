package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Style;

public interface StyleDbService extends AbstractJpaService<Style, Long> {


    List<Style> getAllStyles();


    List<Style> getAllStylesForStatus(boolean status);


    Style getStyleById(String id);


    Style saveStyle(Style style);


    Style activateDeactivateStyle(Style style, boolean status);


    boolean isEntityExistsByName(String name);
}
