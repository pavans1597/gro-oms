package com.groyyo.order.management.db.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.BuyerDbService;
import com.groyyo.order.management.entity.Buyer;
import com.groyyo.order.management.repository.BuyerRepository;


@Service
public class BuyerDbServiceImpl extends AbstractJpaServiceImpl<Buyer, Long, BuyerRepository> implements BuyerDbService {

    @Autowired
    private BuyerRepository buyerRepository;

    @Override
    protected BuyerRepository getJpaRepository() {
        return buyerRepository;
    }

    @Override
    public List<Buyer> getAllBuyers() {
        return buyerRepository.findAll();
    }

    @Override
    public List<Buyer> getAllBuyersForStatus(boolean status) {
        return buyerRepository.findByStatus(status);
    }

    @Override
    public Buyer getBuyerById(String id) {
        return buyerRepository.findByUuid(id);
    }

    @Override
    public Buyer saveBuyer(Buyer buyer) {
        return buyerRepository.saveAndFlush(buyer);
    }

    @Override
    public Buyer activateDeactivateBuyer(Buyer buyer, boolean status) {
        buyer.setStatus(status);
        return buyerRepository.saveAndFlush(buyer);
    }

    @Override
    public boolean isEntityExistsByName(String name) {
        return Objects.nonNull(buyerRepository.findByName(name));
    }
}
