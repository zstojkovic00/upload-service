package com.zeljko.uploadservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class S3Buckets {

    @Value("${aws.s3.buckets.main}")
    private String bucketName;

}