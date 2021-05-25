package com.jobProcessor.handler;

import com.jobProcessor.model.JobDetail;

/**
 * This class is an implementation of the default handler to handle Email type of jobs.
 * Ideally a call can be made to a micro service which handles email processing probably through a REST call
 * The process method needs to be implemented accordingly
 */

public class EmailHandler implements DefaultJobHandler {

    public boolean process(JobDetail item) {
        System.out.println("processed email");
        return true;
    }
}
