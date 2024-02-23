package com.zeljko.uploadservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;
    @Value("${spring.kafka.producer.timeout}")
    private int kafkaProducerTimeout;

    public void sendMessage(String topic, T message) {
        try {
            SendResult<String, T> result = kafkaTemplate.send(topic, message).get(kafkaProducerTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            log.error("Unknown error occurred while sending message:{} to topic:{},error:{}", message.toString(), topic, ex.getMessage());
        }
    }

}
