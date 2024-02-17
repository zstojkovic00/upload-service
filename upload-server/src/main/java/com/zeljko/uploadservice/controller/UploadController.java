package com.zeljko.uploadservice.controller;


import com.zeljko.uploadservice.request.GitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.zeljko.uploadservice.utils.JGit.cloneGit;
import static com.zeljko.uploadservice.utils.StringUtils.generateRandomString;

@RestController
@RequestMapping("api/v1/upload")
@RequiredArgsConstructor
public class UploadController {

    @PostMapping
    public ResponseEntity<String> uploadRepository(@RequestBody GitRequest request) throws Exception {
        String id = generateRandomString();
        cloneGit(request.url(), id);

        return new ResponseEntity<>("id: " + id, HttpStatus.OK);
    }
}
