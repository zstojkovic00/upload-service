package com.zeljko.uploadservice.service;


import com.zeljko.uploadservice.request.GitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.File;



@Service
@Slf4j
@RequiredArgsConstructor
public class UploadService {
    public void uploadFile(GitRequest request, String id) throws Exception {
       File clonedDirectory = cloneGit(request.url(), id);
       File[] allFiles = clonedDirectory.listFiles();

       for (File file : allFiles){
           log.info(String.valueOf(file));
       }

    }

    public File cloneGit(String url, String id) throws Exception {

        File outputDirectory = new File( "out/" + id);
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
