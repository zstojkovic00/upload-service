package com.zeljko.deployservice.npm;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Service
public class NpmCommandExecutor {

    public int execute(String npmCommand, File downloadedFile) {
        int exitCode = 0;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(npmCommand.split(" "));
            processBuilder.directory(downloadedFile);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            exitCode = process.waitFor();
            System.out.println("Process has finished with exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
        return exitCode;
    }
}