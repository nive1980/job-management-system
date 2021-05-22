package com.jobManagement.listener;

import com.jobManagement.model.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;


public class JobMgmtSystmItemProcessorListener implements ItemProcessListener<JobDetail,JobDetail> {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void beforeProcess(JobDetail item)
    {
        logger.info("CustomItemProcessorListener before");
        item.setStatus("RUNNING");
    }

    @Override
    public void afterProcess(JobDetail item, JobDetail result) {
        logger.info("CustomItemProcessorListener after");
    }

    @Override
    public void onProcessError(JobDetail item, Exception e)
    {
        logger.error("CustomItemProcessorListener error");
        item.setStatus("FAILED");
    }
}