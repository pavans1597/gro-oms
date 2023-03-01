package com.groyyo.order.db.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.db.service.SizeDbService;
import com.groyyo.order.entity.Size;
import com.groyyo.order.repository.SizeRepository;


@Service
public class SizeDbServiceImpl extends AbstractJpaServiceImpl<Size, Long, SizeRepository> implements SizeDbService {

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    protected SizeRepository getJpaRepository() {
        return sizeRepository;
    }

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    @Override
    public List<Size> getAllSizesForStatus(boolean status) {
        return sizeRepository.findByStatus(status);
    }

    @Override
    public Size getSizeById(String id) {
        return sizeRepository.findByUuid(id);
    }

    @Override
    public Size saveSize(Size size) {
        return sizeRepository.saveAndFlush(size);
    }

    @Override
    public Size activateDeactivateSize(Size size, boolean status) {
        size.setStatus(status);
        return sizeRepository.saveAndFlush(size);
    }

    @Override
    public boolean isEntityExistsByName(String name) {
        return Objects.nonNull(sizeRepository.findByName(name));
    }
}
