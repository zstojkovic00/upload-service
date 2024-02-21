package com.zeljko.uploadservice.service;


import com.zeljko.uploadservice.config.s3.S3Buckets;
import com.zeljko.uploadservice.request.GitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;


@Service
@Slf4j
@RequiredArgsConstructor
public class UploadService {
    //test

    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final KafkaProducerService kafkaProducer;

    @Value("${spring.kafka.producer.topic.repository.uploaded}")
    private String topicName;

    public void uploadFile(GitRequest request, String id) throws Exception {
        File clonedDirectory = cloneGit(request.url(), id);

        String s3Key = "source/" + id + "/";

        Collection<File> files = FileUtils.listFiles(
                clonedDirectory,
                new RegexFileFilter("^(.*?)"),
                DirectoryFileFilter.DIRECTORY
        );

        files.forEach(System.out::println);
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                try {
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
//                    s3Service.putObject(
//                            s3Buckets.getBucketName(),
//                            s3Key + file.getName(),
//                            fileBytes);
                } catch (IOException e) {
                    log.error("Failed to convert file to file bytes" + e.getMessage());
                }
            });
        }
        log.info("Sending s3Key: {} to topic: {}", s3Key.toLowerCase(), topicName);
        kafkaProducer.sendMessage(topicName, s3Key);
    }


    public File cloneGit(String url, String id) throws Exception {
        File outputDirectory;
        try {
            outputDirectory = new File("out/" + id);
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }

            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(String.valueOf(outputDirectory)))
                    .call();

        } catch (GitAPIException e) {
            log.error("Failed to clone repository:" + e.getMessage());
            throw new Exception("Failed to clone repository: \" + e.getMessage()");
        }
        return outputDirectory;
    }

}
