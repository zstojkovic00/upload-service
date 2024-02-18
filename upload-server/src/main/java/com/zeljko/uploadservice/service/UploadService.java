package com.zeljko.uploadservice.service;


import com.zeljko.uploadservice.config.S3Buckets;
import com.zeljko.uploadservice.request.GitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;


@Service
@Slf4j
@RequiredArgsConstructor
public class UploadService {

    private final S3Service s3Service;
    private final S3Buckets s3Buckets;

    public void uploadFile(GitRequest request, String id) throws Exception {
        File clonedDirectory = cloneGit(request.url(), id);

        Collection<File> files = FileUtils.listFiles(
                clonedDirectory,
                new RegexFileFilter("^(.*?)"),
                DirectoryFileFilter.DIRECTORY
        );

        files.forEach(System.out::println);

        files.forEach(file -> {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                s3Service.putObject(
                        s3Buckets.getBucketName(),
                        id + "/" + file.getName(),
                        fileBytes);
            } catch (IOException e) {
                log.error("Failed to convert file to file bytes" + e.getMessage());
            }
        });
    }


    public File cloneGit(String url, String id) throws Exception {

        File outputDirectory = new File("out/" + id);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
        try {
            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(String.valueOf(outputDirectory)))
                    .call();

        } catch (GitAPIException e) {
            throw new Exception("Failed to clone repository: \" + e.getMessage()");
        }
        return outputDirectory;
    }

}
