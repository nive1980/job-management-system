package com.jobProcessor.handler;

import com.jobProcessor.model.JobDetail;

/**
 * This is an interface for defining the handler for different types of jobs
 */
public interface DefaultJobHandler {
    public boolean process(JobDetail item);
}
