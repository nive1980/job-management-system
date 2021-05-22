package com.jobManagement.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CSVTransformer {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transformer(inputChannel = "fileInputChannel", outputChannel = "fileToJobProcessor")
    public File transform(File aFile) throws Exception {
        logger.info("Pre processing of file if needed'");
        return aFile;
    }
}