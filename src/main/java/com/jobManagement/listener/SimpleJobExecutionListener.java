package com.jobManagement.listener;

import com.jobManagement.repository.JobDetailRepository;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.batch.core.BatchStatus.COMPLETED;
import static org.springframework.batch.core.BatchStatus.STARTED;

@Component
public class SimpleJobExecutionListener  extends JobExecutionListenerSupport {
    private final JobDetailRepository jobDetailRepository;

    public SimpleJobExecutionListener(JobDetailRepository jobDetailRepository) {
        this.jobDetailRepository = jobDetailRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == STARTED) {
            System.out.println("SimpleJobExecutionListener BATCH PROCESS STARTED...!");
        }
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == COMPLETED) {

            System.out.println("SimpleJobExecutionListener BATCH PROCESS COMPLETED...!");

        }
    }

}
