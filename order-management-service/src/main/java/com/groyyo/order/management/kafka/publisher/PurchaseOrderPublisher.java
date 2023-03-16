/**
 * 
 */
package com.groyyo.order.management.kafka.publisher;

import com.groyyo.core.notification.constants.KafkaNotificationConstants;
import com.groyyo.core.pojo.PushNotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderResponseDto;
import com.groyyo.core.kafka.dto.KafkaDTO;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.order.management.constants.KafkaConstants;

import lombok.extern.log4j.Log4j2;

import java.util.Map;

/**
 * @author nipunaggarwal
 *
 */
@Log4j2
@Component
public class PurchaseOrderPublisher {

	@Value("${kafka.quality-management.topic}")
	private String kafkaQualityManagementUpdatesTopic;

	@Autowired
	private NotificationProducer notificationProducer;

	public void publishQcTaskAssignment(PurchaseOrderResponseDto purchaseOrderResponseDto) {

		KafkaDTO kafkaDTO = new KafkaDTO(KafkaConstants.KAFKA_QC_TASK_ASSIGNMENT_TYPE, KafkaConstants.KAFKA_QC_TASK_ASSIGNMENT_SUBTYPE_CREATE, KafkaDTO.class.getName(),
				purchaseOrderResponseDto);

		notificationProducer.publish(kafkaQualityManagementUpdatesTopic, kafkaDTO.getClassName(), kafkaDTO);

		log.info("Qc Task Assignment Published : {} ", kafkaDTO);
	}

	public void publishTaskCompletionNotification(PushNotificationDTO pushNotificationDTO, Map<String,Object> kafkaMetadata) {

		KafkaDTO kafkaDTO = new KafkaDTO(KafkaNotificationConstants.KAFKA_PUSH_NOTIFICATION_TYPE, KafkaNotificationConstants.KAFKA_PUSH_NOTIFICATION_SUBTYPE_EVENT, KafkaDTO.class.getName(),
				pushNotificationDTO);
		kafkaDTO.setMetadata(kafkaMetadata);
		notificationProducer.publish(kafkaQualityManagementUpdatesTopic, kafkaDTO.getClassName(), kafkaDTO);

		log.info("Qc Task Assignment Published : {} ", kafkaDTO);
	}
}
