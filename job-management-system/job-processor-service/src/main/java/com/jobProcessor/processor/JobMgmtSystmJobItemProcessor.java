package com.jobProcessor.processor;

import com.jobProcessor.handler.EmailHandler;
import com.jobProcessor.handler.SmsHandler;
import com.jobProcessor.model.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * This class is an implementation of ItemProcessor to define processing action of the job items.
 * In our case at the beginning of the processing we set the status to running, based on the type of
 * job we call corresponding handlers and in the end change the status to success or failure
 */

public class JobMgmtSystmJobItemProcessor implements ItemProcessor<JobDetail, JobDetail>{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public JobDetail process(final JobDetail item) throws Exception {
        logger.info("In SimpleJobItemProcessor "+item.getName());
        boolean success = false;
        item.setStatus("RUNNING");
        switch (item.getType()) {
            case "EMAIL":
                success =  new EmailHandler().process(item);
                Thread.sleep(5000);
                break;
            case "SMS"  :
                success =  new SmsHandler().process(item);
                break;

            default:
                success = false;
                break;
        }
        if(success) {
            item.setStatus("SUCCESS");
        }
        else {
            item.setStatus("FAILED");
        }
        return item;
    }
}
