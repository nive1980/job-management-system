package com.jobUploader.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;

import static org.springframework.batch.core.BatchStatus.*;

/**
 * This class indicates the listener which performs different tasks during before and
 * after the job execution
 */
@Component
public class CustomJobListener extends JobExecutionListenerSupport {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void beforeJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == STARTED) {
            logger.info("ORDER BATCH PROCESS STARTED...!");
        }
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        String fileName = jobExecution.getJobParameters().getString("fileName");

        if(fileName != null)
        {
            int indx = fileName.indexOf("file:");
            fileName = fileName.substring(5);
            File f= new File(fileName);
            f.delete();
        }
        if (jobExecution.getStatus() == COMPLETED) {
            logger.info("ORDER BATCH PROCESS COMPLETED...!");
        }
    }

}
