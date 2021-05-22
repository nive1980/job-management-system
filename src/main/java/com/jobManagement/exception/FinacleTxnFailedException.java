package com.jobManagement.exception;

public class FinacleTxnFailedException extends Exception {
    public FinacleTxnFailedException(String errorMessage) {
        super(errorMessage);
    }
}
