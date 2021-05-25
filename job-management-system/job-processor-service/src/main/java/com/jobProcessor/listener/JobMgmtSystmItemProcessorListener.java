package com.jobProcessor.listener;

import com.jobProcessor.model.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

/**
 * An implementation of ItemProcessListener to define action needed to be performed during the item processing
 */

public class JobMgmtSystmItemProcessorListener implements ItemProcessListener<JobDetail,JobDetail> {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * This needs to be impelmented according to the task needed to be performed before
     *  Item processing
     * @param item
     */
    @Override
    public void beforeProcess(JobDetail item)
    {
        logger.info("CustomItemProcessorListener before");
        item.setStatus("RUNNING");
    }

    /**
     * This needs to be impelmented according to the task needed to be performed after
     * item processing
     * @param item
     * @param result
     */

    @Override
    public void afterProcess(JobDetail item, JobDetail result) {
        logger.info("CustomItemProcessorListener after");
    }

    /**
     This needs to be impelmented according to the task needed to be performed during error of
     * item processing.For our case we are updating the job detail status to failed
     * @param item
     * @param e
     */
    @Override
    public void onProcessError(JobDetail item, Exception e)
    {
        logger.error("CustomItemProcessorListener error");
        item.setStatus("FAILED");
    }
}