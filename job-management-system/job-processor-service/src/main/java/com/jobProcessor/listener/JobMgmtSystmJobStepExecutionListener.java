package com.jobProcessor.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
public class JobMgmtSystmJobStepExecutionListener implements StepExecutionListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.info("SimpleJobStepExecutionListener ---- before  ");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("SimpleJobStepExecutionListener  ---- after  ");
        return ExitStatus.COMPLETED;
    }
}