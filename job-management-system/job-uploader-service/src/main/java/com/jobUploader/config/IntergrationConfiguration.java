package com.jobUploader.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * This class can be used to implement the Spring Integration feature
 * for polling a folder for files and integrate it with the spring batch process
 * This is an alternate way instead of using he scheduler.
 * The Component annotation is commented out
 */
//@Component
public class IntergrationConfiguration {

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Bean
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Bean
    public DirectChannel pgpFileProcessor() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "30000"))
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        source.setDirectory(new File(uploadDir));
        source.setFilter(new SimplePatternFileListFilter("*.csv"));
        source.setScanEachPoll(true);
        source.setUseWatchService(true);
        logger.info("reached here int "+source);
        return source;
    }

    @Bean
    @ServiceActivator(inputChannel = "jobChannel", outputChannel = "nullChannel")
    protected JobLaunchingMessageHandler launcher(JobLauncher jobLauncher) {
        return new JobLaunchingMessageHandler(jobLauncher);
    }
}