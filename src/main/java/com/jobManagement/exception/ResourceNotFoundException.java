package com.jobManagement.exception;

public class ResourceNotFoundException extends Exception {
  public ResourceNotFoundException(String error) {
    super(error);
  }
}
