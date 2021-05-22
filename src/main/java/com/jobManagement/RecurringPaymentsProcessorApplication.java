package com.jobManagement;

//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class RecurringPaymentsProcessorApplication {

  public static void main(String[] args) {
    SpringApplication.run(RecurringPaymentsProcessorApplication.class, args);
  }

}
