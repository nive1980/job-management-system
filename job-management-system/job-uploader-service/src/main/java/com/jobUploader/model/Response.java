package com.jobUploader.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * The Response object to be sent to the UI
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private Boolean success;
    private String message;
    private Integer status;
    private String error;
}
