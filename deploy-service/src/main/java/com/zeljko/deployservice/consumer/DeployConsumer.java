package com.zeljko.deployservice.consumer;

import com.zeljko.deployservice.config.s3.S3Buckets;
import com.zeljko.deployservice.npm.NpmCommandExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeployConsumer {

    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final NpmCommandExecutor npm;

    @KafkaListener(
            topics = "build-project",
            groupId = "group.id.test",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void deploy(String key) {
        log.info("Recieved message: {}", key);

//        File downloadedFile = s3Service.downloadS3Folder(s3Buckets.getBucketName(), key);

        // build + key
        File downloadedFile = new File("build/test-app/");

        int exitCode = npm.execute("npm install", downloadedFile);

        if (exitCode != 0) {
            exitCode = npm.execute("npm run build", downloadedFile);
        }

        log.info("Process has finished with exit code: {}", exitCode);
    }


}
