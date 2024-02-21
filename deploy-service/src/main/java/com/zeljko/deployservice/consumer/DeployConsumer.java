package com.zeljko.deployservice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeployConsumer {

    @KafkaListener(
            topics = "build-project",
            groupId = "group.id.test",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void deploy(String id) {

        log.info("Recieved message: {}", id);
    }

}
