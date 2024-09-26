package com.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Log4j2
@RestController
@RequestMapping("kafka")
@RequiredArgsConstructor
public class UserController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private final ObjectMapper mapper;

    private static final String TOPIC = "Kafka_Example";

    @GetMapping("/publish/{name}")
    public String post(@PathVariable("name") final String name) throws JsonProcessingException {
        User user = new User(name, "Technology", 13000L);
        kafkaTemplate.send(TOPIC, mapper.writeValueAsString(user));

        return "Published successfully";
    }

    @GetMapping("/add-topic/{name}")
    public String addTopic(@PathVariable("name") final String name) throws JsonProcessingException {
        User user = new User(name, "Add Topic Exp", 12000L);
        kafkaTemplate.send(name, mapper.writeValueAsString(user));

        return "Add Topic success!!!!!";
    }

    @GetMapping(value = "reply-kafka")
    public String replyKafka() throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        User user = new User("SENDING TO REPLY TOPIC", "", 10L);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("request-topic", mapper.writeValueAsString(user));

        RequestReplyFuture<String, String, String> requestReplyFuture = replyingKafkaTemplate.sendAndReceive(producerRecord);
        SendResult<String, String> sendResult = requestReplyFuture.getSendFuture().get();
        log.info("Message sent successfully: {}", sendResult.getProducerRecord().value());

        ConsumerRecord<String, String> consumerRecord = requestReplyFuture.get(10, TimeUnit.SECONDS);
        String value = consumerRecord.value();
        log.info("Message received: {}", value);
        return value;
    }
}
