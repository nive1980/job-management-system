package com.jobManagement.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CsvFileProcessor {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Transformer(inputChannel = "fileInputChannel", outputChannel = "fileToJobProcessor")
    public File transform(File aFile) throws Exception {
        logger.info("Any preprocessingto be done here");
        return aFile;
    }
}