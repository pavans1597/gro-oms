package com.groyyo.order.management.service.impl;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.base.constants.InterceptorConstants;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.userservice.UserResponseDto;
import com.groyyo.core.enums.QcUserType;
import com.groyyo.core.notification.enums.Event;
import com.groyyo.core.notification.enums.EventType;
import com.groyyo.core.notification.enums.NotificationSubType;
import com.groyyo.core.pojo.PushNotificationDTO;
import com.groyyo.order.management.adapter.PushNotificationAdapter;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.http.service.UserManagementHttpService;
import com.groyyo.order.management.kafka.publisher.PurchaseOrderPublisher;
import com.groyyo.order.management.service.NotificationTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationTriggerServiceImpl implements NotificationTriggerService {
    @Autowired
    private PurchaseOrderPublisher purchaseOrderPublisher;

    @Autowired
    private UserManagementHttpService userManagementHttpService;

    @Override
    public void notifyOrderCompletion(PurchaseOrder purchaseOrder) {
        String factoryId = HeaderUtil.getFactoryIdHeaderValue();
        //TODO : based on product manager requirement we decide which api to call

        ResponseDto<List<UserResponseDto>> usersByRole = userManagementHttpService.getUsersByDepartmentAndRole(factoryId,"FINISHING", QcUserType.CHECKER);
        List<String> userIds = usersByRole.getData().stream().map(UserResponseDto::getId).collect(Collectors.toList());

        PushNotificationDTO pushNotificationDTO = PushNotificationAdapter.buildPushNotificationDto(
                purchaseOrder.getName() + " Completed",
                purchaseOrder.getName() + " Completed for " + purchaseOrder.getProductName(),
                null,
                userIds,
                Event.ORDER_COMPLETION,
                EventType.BULK,
                NotificationSubType.TRANSACTIONAL
        );                                                                    // Make push Notification Object

        publishTaskCompletionNotification(pushNotificationDTO,factoryId);
    }

    private void publishTaskCompletionNotification(PushNotificationDTO pushNotificationDTO,String factoryId) {

        Map<String, Object> metadata = Map.of(InterceptorConstants.HEADER_FACTORY_ID_NAME, factoryId); // Creating of Metadata

        purchaseOrderPublisher.publishPushNotificationEvent(pushNotificationDTO,metadata);
    }



}
