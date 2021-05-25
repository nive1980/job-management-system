package com.jobProcessor.listener;

import com.jobProcessor.model.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

public class JobMgmtSystmStepItemProcessListener implements ItemProcessListener<JobDetail, JobDetail> {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void beforeProcess(JobDetail item) {
        logger.info("ItemProcessListener - beforeProcess"+item.getName());
        item.setStatus("RUNNING");
    }

    @Override
    public void afterProcess(JobDetail jobDetail1, JobDetail jobDetail2) {
        logger.info("ItemProcessListener - afterProcess"+jobDetail1.getName());
    }

    @Override
    public void onProcessError(JobDetail item, Exception e) {
        logger.error("ItemProcessListener - onProcessError");
    }
}