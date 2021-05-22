package com.jobManagement.handler;

import com.jobManagement.model.JobDetail;

public class SmsHandler implements  DefaultJobHandler {
    public boolean process(JobDetail item) {
        System.out.println("processed sms");
        return true;
    }
}
