package com.jobProcessor.listener;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
public class JobMgmtSystmStepListener implements StepExecutionListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("StepExecutionListener  ---- before  ");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("StepExecutionListener  ---- after  ");
        String name =         (String)      stepExecution.getJobParameters().getParameters().get("fileName").getValue();
        logger.info("name "+name);
        File file = new File(name);
        if(file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
            }
        }
        return ExitStatus.COMPLETED;
    }
}