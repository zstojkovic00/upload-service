package com.zeljko.uploadservice.controller;


import com.zeljko.uploadservice.request.GitRequest;
import com.zeljko.uploadservice.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("api/v1/deploy")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity<String> uploadRepository(@RequestBody GitRequest request) {
        try {
            UUID id = UUID.randomUUID();
            uploadService.uploadFile(request, id.toString());
            return new ResponseEntity<>("id: " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
