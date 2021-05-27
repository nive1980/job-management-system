package com.jobProcessor;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchTestConfig {
    @Bean
    JobRepositoryTestUtils jobRepositoryTestUtils () {
        return new JobRepositoryTestUtils();
    }
    @Bean
    JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils() {

            @Override
            @Autowired
            public JobParameters getUniqueJobParameters() {
                return  defaultJobParameters();
            }
            private JobParameters defaultJobParameters() {
                JobParametersBuilder paramsBuilder = new JobParametersBuilder();
                return paramsBuilder.toJobParameters();
            }

        };
    }

}