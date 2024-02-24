package com.zeljko.deployservice.consumer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;


@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    public File downloadS3Folder(String bucketName, String folderName) {

        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix("source/" + folderName)
                .build();

        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

        File outputDirectory = new File("build/" + folderName);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }


        for (S3Object object : listObjectsResponse.contents()) {
            String key = object.key();
            String fileName = key.substring(key.lastIndexOf("/") + 1);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();


            try (ResponseInputStream<GetObjectResponse> objectResponse = s3Client.getObject(getObjectRequest);
                 FileOutputStream fos = new FileOutputStream(new File(outputDirectory, fileName))) {
                objectResponse.transferTo(fos);
                log.info("Successfully downloaded file: {} from s3 bucket: {}", fileName, bucketName);
            } catch (IOException e) {
                throw new RuntimeException("Error downloading file from S3: " + e.getMessage(), e);
            }
        }
        return outputDirectory;
    }
}
