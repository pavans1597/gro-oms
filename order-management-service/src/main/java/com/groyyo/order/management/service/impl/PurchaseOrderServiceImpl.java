package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.response.PurchaseOrderResponseDto;
import com.groyyo.order.management.dto.response.StyleResponseDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.order.management.adapter.PurchaseOrderAdapter;
import com.groyyo.order.management.db.service.PurchaseOrderDbService;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.service.PurchaseOrderService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    @Autowired
    private PurchaseOrderDbService purchaseOrderDbService;

    @Override
    public List<PurchaseOrderResponseDto> getAllPurchaseOrders(Boolean status) {

        log.info("Serving request to get all purchaseOrders");

        List<PurchaseOrder> purchaseOrderEntities = Objects.isNull(status) ? purchaseOrderDbService.getAllPurchaseOrders()
                : purchaseOrderDbService.getAllPurchaseOrdersForStatus(status);

        if (CollectionUtils.isEmpty(purchaseOrderEntities)) {
            log.error("No PurchaseOrders found in the system");
            return new ArrayList<>();
        }

        return PurchaseOrderAdapter.buildResponsesFromEntities(purchaseOrderEntities);
    }

    @Override
    public PurchaseOrderResponseDto getPurchaseOrderById(String id) {

        log.info("Serving request to get a purchaseOrder by id:{}", id);

        PurchaseOrder purchaseOrder = purchaseOrderDbService.getPurchaseOrderById(id);

        if (Objects.isNull(purchaseOrder)) {
            String errorMsg = "PurchaseOrder with id: " + id + " not found in the system ";
            throw new NoRecordException(errorMsg);
        }

        return PurchaseOrderAdapter.buildResponseFromEntity(purchaseOrder);
    }

    @Override
    public PurchaseOrderResponseDto addPurchaseOrder(PurchaseOrderRequestDto purchaseOrderRequestDto, StyleResponseDto styleResponse) {

        log.info("Serving request to add a purchaseOrder with request object:{}", purchaseOrderRequestDto);



        PurchaseOrder purchaseOrder = PurchaseOrderAdapter.buildPurchaseOrderFromRequest(purchaseOrderRequestDto, styleResponse);

        purchaseOrder = purchaseOrderDbService.savePurchaseOrder(purchaseOrder);



        if (Objects.isNull(purchaseOrder)) {
            log.error("Unable to add purchaseOrder for object: {}", purchaseOrderRequestDto);
            return null;
        }

        return PurchaseOrderAdapter.buildResponseFromEntity(purchaseOrder);
    }

    @Override
    public PurchaseOrderResponseDto updatePurchaseOrder(PurchaseOrderRequestDto purchaseOrderRequestDto) {

        log.info("Serving request to update a purchaseOrder with request object:{}", purchaseOrderRequestDto);

        PurchaseOrder purchaseOrder = purchaseOrderDbService.getPurchaseOrderById(purchaseOrderRequestDto.getId());

        if (Objects.isNull(purchaseOrder)) {
            log.error("PurchaseOrder with id: {} not found in the system", purchaseOrderRequestDto.getId());
            return null;
        }

        runValidations(purchaseOrderRequestDto);

        purchaseOrder = PurchaseOrderAdapter.clonePurchaseOrderWithRequest(purchaseOrderRequestDto, purchaseOrder);

        purchaseOrderDbService.savePurchaseOrder(purchaseOrder);

        return PurchaseOrderAdapter.buildResponseFromEntity(purchaseOrder);
    }

    private boolean isEntityExistsWithName(String name) {

        return StringUtils.isNotBlank(name) && purchaseOrderDbService.isEntityExistsByName(name);
    }

    private void runValidations(PurchaseOrderRequestDto purchaseOrderRequestDto) {

        validateName(purchaseOrderRequestDto);
    }



    private void validateName(PurchaseOrderRequestDto purchaseOrderRequestDto) {

        if (isEntityExistsWithName(purchaseOrderRequestDto.getName())) {
            String errorMsg = "PurchaseOrder cannot be created/updated as record already exists with name: " + purchaseOrderRequestDto.getName();
            throw new RecordExistsException(errorMsg);
        }
    }
}
