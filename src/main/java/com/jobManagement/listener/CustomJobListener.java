package com.jobManagement.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;

import static org.springframework.batch.core.BatchStatus.*;

@Component
public class CustomJobListener extends JobExecutionListenerSupport {

    @Override
    public void beforeJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == STARTED) {
            System.out.println("ORDER BATCH PROCESS STARTED...!");
        }
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        String fileName = jobExecution.getJobParameters().getString("fileName");

        if(fileName != null)
        {
            int indx = fileName.indexOf("file:");
            fileName = fileName.substring(5);
            System.out.println("fileName...!"+fileName);
            File f= new File(fileName);
            f.delete();
        }
        if (jobExecution.getStatus() == COMPLETED) {

            System.out.println("ORDER BATCH PROCESS COMPLETED...!");

        }
    }

}
