package com.groyyo.order.management.service.db.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.service.db.service.PartDbService;
import com.groyyo.order.management.service.entity.Part;
import com.groyyo.order.management.service.repository.PartRepository;


@Service
public class PartDbServiceImpl extends AbstractJpaServiceImpl<Part, Long, PartRepository> implements PartDbService {

    @Autowired
    private PartRepository partRepository;

    @Override
    protected PartRepository getJpaRepository() {
        return partRepository;
    }

    @Override
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    @Override
    public List<Part> getAllPartsForStatus(boolean status) {
        return partRepository.findByStatus(status);
    }

    @Override
    public Part getPartById(String id) {
        return partRepository.findByUuid(id);
    }

    @Override
    public Part savePart(Part part) {
        return partRepository.saveAndFlush(part);
    }

    @Override
    public Part activateDeactivatePart(Part part, boolean status) {
        part.setStatus(status);
        return partRepository.saveAndFlush(part);
    }

    @Override
    public boolean isEntityExistsByName(String name) {
        return Objects.nonNull(partRepository.findByName(name));
    }
}
