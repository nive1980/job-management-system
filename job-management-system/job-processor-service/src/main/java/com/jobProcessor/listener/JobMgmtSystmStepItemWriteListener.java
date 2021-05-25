package com.jobProcessor.listener;

import java.util.List;

import com.jobProcessor.model.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

public class JobMgmtSystmStepItemWriteListener implements ItemWriteListener<JobDetail> {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void beforeWrite(List<? extends JobDetail> items) {
        logger.info("ItemWriteListener - beforeWrite");
    }

    @Override
    public void afterWrite(List<? extends JobDetail> items) {
        logger.info("ItemWriteListener - afterWrite");
    }

    @Override
    public void onWriteError(Exception exception, List<? extends JobDetail> items) {
        logger.error("ItemWriteListener - onWriteError");
    }
}
