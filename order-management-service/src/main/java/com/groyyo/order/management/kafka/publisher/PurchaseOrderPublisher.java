/**
 * 
 */
package com.groyyo.order.management.kafka.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderResponseDto;
import com.groyyo.core.kafka.dto.KafkaDTO;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.order.management.constants.KafkaConstants;

import lombok.extern.log4j.Log4j2;

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

}
