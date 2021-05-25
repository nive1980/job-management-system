package com.jobUploader.processor;

import com.jobUploader.model.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.io.IOException;
import java.util.Date;

/**
 * The processor which processes the job items before sending it to writer.We set the status to QUEUED
 */

public class FileUploadJobDetailProcessor implements ItemProcessor<JobDetail, JobDetail> {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public JobDetail process(final JobDetail jobDetail) throws IOException {
        logger.info("reached FileUploadJobDetailProcessor");

       if(jobDetail.isExecuteImmediate()) {
            jobDetail.setExecutionDate(new Date());
        }
        jobDetail.setStatus("QUEUED");
        return jobDetail;
    }
}
