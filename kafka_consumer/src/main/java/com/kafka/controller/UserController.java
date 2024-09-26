package com.kafka.controller;

import com.kafka.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("kafka")
public class UserController {

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    private static final String TOPIC = "Kafka_Example";

    @GetMapping("/publish/{name}")
    public String post(@PathVariable("name") final String name) {

        kafkaTemplate.send(TOPIC, new User(name, "Technology", 13000L));

        return "Published successfully";
    }

    @GetMapping("/add-topic/{name}")
    public String addTopic(@PathVariable("name") final String name) {

        kafkaTemplate.send(name, new User(name, "Add Topic Exp", 12000L));

        return "Add Topic success!!!!!";
    }

//    @GetMapping("/publishList")
//    public String postData() {
//
//        List<User> lst = new ArrayList<User>();
//        lst.add(new User("Trung", "Tech", 1L));
//        lst.add(new User("Hoang", "Tech", 1L));
//        lst.add(new User("Hai", "Tech", 1L));
//        lst.add(new User("Trang", "Tech", 1L));
//
//        kafkaTemplate.send(TOPIC, lst);
//
//        return "Published successfully";
//    }
}
