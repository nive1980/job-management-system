package com.jobManagement.handler;

import com.jobManagement.model.JobDetail;

public class EmailHandler implements DefaultJobHandler {

    public boolean process(JobDetail item) {
        System.out.println("processed email");
        return true;
    }
}
