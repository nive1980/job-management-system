package com.jobManagement.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
public class CustomStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("StepExecutionListener  ---- before  ");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("StepExecutionListener  ---- after  ");
        String name =         (String)      stepExecution.getJobParameters().getParameters().get("fileName").getValue();
        System.out.print("name "+name);
        File file = new File(name);
        if(file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
            }
        }
        return null;
    }
}