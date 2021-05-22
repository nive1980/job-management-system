package com.jobManagement.handler;

import com.jobManagement.model.JobDetail;

public interface DefaultJobHandler {
    public boolean process(JobDetail item);
}
