package com.groyyo.order.management.service;

import com.groyyo.order.management.entity.PurchaseOrder;

public interface NotificationTriggerService {

    void notifyOrderCompletion(PurchaseOrder purchaseOrder);
}
