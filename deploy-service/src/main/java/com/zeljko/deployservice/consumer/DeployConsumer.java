package com.zeljko.deployservice.consumer;

import com.zeljko.deployservice.config.s3.S3Buckets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeployConsumer {

    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    @KafkaListener(
            topics = "build-project",
            groupId = "group.id.test",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void deploy(String key) {
        log.info("Recieved message: {}", key);

        s3Service.downloadS3Folder(s3Buckets.getBucketName(), key);

    }


}
