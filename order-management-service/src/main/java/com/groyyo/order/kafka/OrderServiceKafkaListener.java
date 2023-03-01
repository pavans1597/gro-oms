package com.groyyo.order.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@KafkaListener(topics = "multiType", clientIdPrefix = "json",
        containerFactory = "kafkaListenerContainerFactory")
public class OrderServiceKafkaListener {
    private static final Map<Long, Integer> OFFSET_ID_TO_RETRY_COUNT = new HashMap<>();

    private static final int MAX_RETRY_COUNT = 3;


    private static int getTenantRetryCount(long offsetId) {
        if (OFFSET_ID_TO_RETRY_COUNT.containsKey(offsetId)) {
            OFFSET_ID_TO_RETRY_COUNT.put(offsetId, OFFSET_ID_TO_RETRY_COUNT.get(offsetId) + 1);
        } else {
            OFFSET_ID_TO_RETRY_COUNT.put(offsetId, 1);
        }
        return OFFSET_ID_TO_RETRY_COUNT.get(offsetId);
    }

}
