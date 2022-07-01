package com.healthcareApp.healthcareApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MessagingService<T> {
    @Autowired
    private KafkaTemplate<String, T> kafkaTemplate;

    public void sendMessageToTopic(String topic, T t) {
        Message<T> message = MessageBuilder
                .withPayload(t)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        this.kafkaTemplate.send(message);
    }

}
