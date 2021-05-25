package com.jobUploader;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * This class is used to start the Job Uploader spring boot service
 */
@SpringBootApplication
@EnableAsync
@EnableBatchProcessing
@EnableScheduling
public class JobUploaderServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(JobUploaderServiceApplication.class, args);
  }

}
