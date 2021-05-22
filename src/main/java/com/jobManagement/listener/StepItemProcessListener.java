package com.jobManagement.listener;

import com.jobManagement.model.JobDetail;
import org.springframework.batch.core.ItemProcessListener;

public class StepItemProcessListener implements ItemProcessListener<JobDetail, JobDetail> {



    @Override
    public void beforeProcess(JobDetail item) {
        System.out.println("ItemProcessListener - beforeProcess"+item.getName());
        item.setStatus("RUNNING");
    }

    @Override
    public void afterProcess(JobDetail jobDetail1, JobDetail jobDetail2) {
        System.out.println("ItemProcessListener - afterProcess"+jobDetail1.getName());
        jobDetail2.setStatus("SUCCESS");
    }

    @Override
    public void onProcessError(JobDetail item, Exception e) {
        System.out.println("ItemProcessListener - onProcessError");
    }
}