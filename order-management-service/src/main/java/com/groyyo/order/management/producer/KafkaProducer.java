//package com.groyyo.order.producer;
//
//import com.groyyo.order.dto.request.TaskAssinmentDto;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.KafkaTemplate;
//
//@Configuration
//public class KafkaProducer {
//	
//    @Autowired
//    private KafkaTemplate<String, TaskAssinmentDto> kafkaTemplate;
//
//    public void assignTask(TaskAssinmentDto msg) {
//        assignQcTaskAssinment(msg);
//    }
//
//    private void assignQcTaskAssinment(TaskAssinmentDto msg) {
//        ProducerRecord<String, TaskAssinmentDto> record = new ProducerRecord<>("qc-task-topic", "my-key", msg);
//        record.headers().add("foo", "bar".getBytes());
//        kafkaTemplate.send(record);
//    }
//}