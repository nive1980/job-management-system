package com.jobManagement.listener;

import java.util.List;

import com.jobManagement.model.JobDetail;
import org.springframework.batch.core.ItemWriteListener;

public class StepItemWriteListener implements ItemWriteListener<JobDetail> {

    @Override
    public void beforeWrite(List<? extends JobDetail> items) {
        System.out.println("ItemWriteListener - beforeWrite");
    }

    @Override
    public void afterWrite(List<? extends JobDetail> items) {
        System.out.println("ItemWriteListener - afterWrite");
    }

    @Override
    public void onWriteError(Exception exception, List<? extends JobDetail> items) {
        System.out.println("ItemWriteListener - onWriteError");
    }
}
