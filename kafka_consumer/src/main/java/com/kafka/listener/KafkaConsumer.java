package com.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "Kafka_Example", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }


    @KafkaListener(topics = "Kafka_Example_json", groupId = "group_json")
    public void consumeJson(User user) {
        log.info("Consumed JSON Message: {}", user);
    }

    @KafkaListener(topics = "request-topic")
    @SendTo("reply-topic")
    public User receiveMsg(String message) throws JsonProcessingException {
        log.info("Received message: {}", message);
        User user = objectMapper.readValue(message, User.class);
        user.setDept("IT");
        user.setSalary(12L);
        return user;
    }

}
