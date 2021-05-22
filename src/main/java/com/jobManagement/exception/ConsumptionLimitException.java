package com.jobManagement.exception;

public class ConsumptionLimitException extends Exception {
  public ConsumptionLimitException(String errorMessage) {
    super(errorMessage);
  }
}
