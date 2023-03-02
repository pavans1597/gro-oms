package com.groyyo.order.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.entity.Buyer;

public interface BuyerDbService extends AbstractJpaService<Buyer, Long> {


    List<Buyer> getAllBuyers();


    List<Buyer> getAllBuyersForStatus(boolean status);


    Buyer getBuyerById(String id);


    Buyer saveBuyer(Buyer buyer);


    Buyer activateDeactivateBuyer(Buyer buyer, boolean status);


    boolean isEntityExistsByName(String name);
}
