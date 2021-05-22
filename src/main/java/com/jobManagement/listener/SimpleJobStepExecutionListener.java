package com.jobManagement.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
public class SimpleJobStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("SimpleJobStepExecutionListener ---- before  ");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("SimpleJobStepExecutionListener  ---- after  ");
        return null;
    }
}