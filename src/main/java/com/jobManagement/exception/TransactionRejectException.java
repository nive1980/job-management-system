package com.jobManagement.exception;

public class TransactionRejectException extends Exception {
  public TransactionRejectException(String error) {
    super(error);
  }
}
