package com.jobProcessor.listener;

import com.jobProcessor.repository.JobDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.batch.core.BatchStatus.COMPLETED;
import static org.springframework.batch.core.BatchStatus.STARTED;

/**
 * A class to define the implementation of JobExecutionListenerSupport to define actions to be performed during job execution
 */
@Component
public class JobMgmtSystmJobExecutionListener extends JobExecutionListenerSupport {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JobDetailRepository jobDetailRepository;

    public JobMgmtSystmJobExecutionListener(JobDetailRepository jobDetailRepository) {
        this.jobDetailRepository = jobDetailRepository;
    }

    /**
     * This needs to be impelmented according to the task needed to be performed before
     *  Job processing
     * @param jobExecution
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == STARTED) {
            logger.info("SimpleJobExecutionListener BATCH PROCESS STARTED...!");
        }
    }

    /**
     * This needs to be impelmented according to the task needed to be performed after
     *  Job processing
     * @param jobExecution
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == COMPLETED) {
            logger.info("SimpleJobExecutionListener BATCH PROCESS COMPLETED...!");
        }
    }

}
