package com.jobProcessor.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;

/**
 * An implementation of ItemListenerSupport to define action needed to be performed during the
 * occurence of error during the Job execution
 */
public class JobMgmtSystmJobItemFailureLoggerListener extends ItemListenerSupport {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * This method handles the error during read operation
     * @param ex
     */
    public void onReadError(Exception ex) {
        logger.error("Encountered error on read");
    }

    /**
     * This method handles the error during write operation
     * @param ex
     * @param item
     */

    public void onWriteError(Exception ex, Object item) {
        logger.error("Encountered error on write");
    }

    /**
     * This method handles the error during processing operation
     * @param item
     * @param e
     */

    @Override
    public void onProcessError(Object item, Exception e) {
        logger.error("onProcessError");
    }

}
