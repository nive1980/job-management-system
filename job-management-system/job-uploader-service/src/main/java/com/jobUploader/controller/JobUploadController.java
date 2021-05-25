package com.jobUploader.controller;

import com.jobUploader.model.Response;
import com.jobUploader.service.FileStorageService;
import com.jobUploader.service.JobDetailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * This class is used to handle the REST calls for Job upload from the UI
 */
@RestController
@RequiredArgsConstructor
public class JobUploadController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FileStorageService fileStorageService;
    private final JobDetailService jobDetailService;

    /**
     * This method intercepts the call to upload the csv files
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadFile")
    public ResponseEntity<Response> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        logger.info("reached JobUploadController file: "+file);
        fileStorageService.store(file);
        Response response = new Response();
        response.setStatus(HttpStatus.OK.value());
        response.setSuccess(true);
        response.setMessage("Jobs CSV file Uploaded Successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * This rest cll returns the list of job records and their status
     * @return
     */
    @GetMapping
    @RequestMapping("getJobDetails")
    public ResponseEntity<?> getJobDetails() {
        /*logger.info*/System.out.println("reached getJobDetails file:" );
        return ResponseEntity.ok().body(jobDetailService.getJobDetails());
    }

}
