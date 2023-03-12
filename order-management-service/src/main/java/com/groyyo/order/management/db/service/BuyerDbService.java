package com.groyyo.order.management.db.service;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Buyer;

import java.util.List;

public interface BuyerDbService extends AbstractJpaService<Buyer, Long> {


    List<Buyer> getAllBuyers(String factoryId);


    List<Buyer> getAllBuyersForStatus(boolean status,String factoryId);


    Buyer getBuyerById(String id);


    Buyer saveBuyer(Buyer buyer);


    Buyer activateDeactivateBuyer(Buyer buyer, boolean status);


    boolean isEntityExistsByName(String name);
}
