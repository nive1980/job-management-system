package com.jobProcessor;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * This class starts the Job processor spring boot application
 */
@SpringBootApplication
@EnableAsync
@EnableBatchProcessing
@EnableScheduling
public class JobProcessorServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(JobProcessorServiceApplication.class, args);
  }

}
