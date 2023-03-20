package com.groyyo.order.management.adapter;


import com.groyyo.core.notification.enums.Event;
import com.groyyo.core.notification.enums.EventType;
import com.groyyo.core.notification.enums.NotificationSubType;
import com.groyyo.core.notification.enums.NotificationType;
import com.groyyo.core.pojo.PushNotificationDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PushNotificationAdapter {


    public static PushNotificationDTO buildPushNotificationDto(
            String title, String body,
            String imgUrl, List<String> userIds,
            Event event, EventType type,
            NotificationSubType notificationSubType) {

        return PushNotificationDTO.builder()
                .title(title)
                .body(body)
				.imageUrl(imgUrl)
                .userIds(userIds)
                .event(event)
                .eventType(type)
                .notificationType(NotificationType.PUSH_NOTIFICATION)
                .notificationSubType(notificationSubType)
                .build();
    }
}
