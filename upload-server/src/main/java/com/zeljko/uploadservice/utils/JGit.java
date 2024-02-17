package com.zeljko.uploadservice.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;


import java.io.File;


public class JGit {

    public static void cloneGit(String url, String id) throws Exception {

        File outputDirectory = new File(System.getProperty("user.div"), "out/" + id);
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
    }
}
