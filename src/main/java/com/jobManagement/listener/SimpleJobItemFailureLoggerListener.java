package com.jobManagement.listener;

import org.springframework.batch.core.listener.ItemListenerSupport;

public class SimpleJobItemFailureLoggerListener extends ItemListenerSupport {


    public void onReadError(Exception ex) {
        System.out.println("Encountered error on read");
    }

    public void onWriteError(Exception ex, Object item) {
        System.out.println("Encountered error on write");
    }
    @Override
    public void onProcessError(Object item, Exception e) {
        System.out.println("onProcessError");
    }

}
