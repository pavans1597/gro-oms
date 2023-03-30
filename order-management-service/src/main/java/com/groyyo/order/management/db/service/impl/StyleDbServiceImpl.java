package com.groyyo.order.management.db.service.impl;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

import com.groyyo.order.management.entity.Style;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.StyleDbService;
import com.groyyo.order.management.repository.StyleRepository;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class StyleDbServiceImpl extends AbstractJpaServiceImpl<Style, Long, StyleRepository> implements StyleDbService {

    @Autowired
    private StyleRepository styleRepository;

    @Override
    protected StyleRepository getJpaRepository() {
        return styleRepository;
    }

    @Override
    public List<Style> getAllStyles(String factoryId) {

        return (Objects.isNull(factoryId) ? styleRepository.findAll() : styleRepository.findAllByFactoryId(factoryId));
    }

    @Override
    public List<Style> getAllStylesForStatus(boolean status, String factoryId) {

        return Objects.nonNull(factoryId) ? styleRepository.findByStatusAndFactoryId(status, factoryId) : styleRepository.findByStatus(status);
    }

    @Override
    public Style getStyleById(String id) {
        return styleRepository.findByUuid(id);
    }

    @Override
    public Style saveStyle(Style style) {
        return styleRepository.saveAndFlush(style);
    }

    @Override
    public Style activateDeactivateStyle(Style style, boolean status) {
        style.setStatus(status);
        return styleRepository.saveAndFlush(style);
    }

    @Override
    public boolean isEntityExistsByStyleNumber(String styleNumber) {

        return Objects.nonNull(styleRepository.findByStyleNumber(styleNumber));

    }

    @Override
    public Style findOrCreate(Style style) {
        Style entity = styleRepository.findByStyleNumberAndFactoryId(style.getStyleNumber(), style.getFactoryId());
        if (Objects.isNull(entity)) {
            entity = style;
            save(entity);
        } else {
            if (!StringUtils.equals(entity.getName(), style.getName())) {
                throw new InputMismatchException("Style Name is different for the provided styleNumber: " + entity.getStyleNumber());
            }
            if (!StringUtils.equals(entity.getProductId(), style.getProductId())) {
                throw new InputMismatchException(style.getProductName() + " - this product name is not attached with this style- " + style.getName());
            }
        }
        return entity;
    }
}
