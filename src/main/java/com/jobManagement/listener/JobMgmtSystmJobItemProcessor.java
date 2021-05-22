package com.jobManagement.listener;

import com.jobManagement.handler.EmailHandler;
import com.jobManagement.handler.SmsHandler;
import com.jobManagement.model.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class JobMgmtSystmJobItemProcessor implements ItemProcessor<JobDetail, JobDetail>{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public JobDetail process(final JobDetail item) throws Exception {
        logger.info("In SimpleJobItemProcessor ");
        boolean success = false;
        item.setStatus("RUNNING");
        switch (item.getType()) {
            case "EMAIL":
                success =  new EmailHandler().process(item);
                Thread.sleep(5000);
            case "SMS"  :
                success =  new SmsHandler().process(item);
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
