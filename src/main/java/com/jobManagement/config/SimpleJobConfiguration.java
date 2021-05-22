package com.jobManagement.config;


import com.jobManagement.listener.JobMgmtSystmItemProcessorListener;
import com.jobManagement.listener.JobMgmtSystmJobItemProcessor;
import com.jobManagement.model.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


//@Configuration
public class SimpleJobConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    EntityManagerFactory emf;
    @Autowired
    private ItemReader<JobDetail> jobManagementReader;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Bean
    public Step jobManagementStep() throws Exception {
        logger.info("In Job Management Step");
        return stepBuilderFactory
                .get("jobManagementStep")
                .<JobDetail, JobDetail>chunk(10)
                .reader(jobManagementReader)
                .processor(jobManagementProcessor())
                .writer(jobManagementWriter())
                .listener(jobManagementItemProcessorlistener())
                .build();
    }
    @Bean
    public ItemProcessor<JobDetail,JobDetail> jobManagementProcessor()  {
        logger.info("In Job Management Processor");
        return new JobMgmtSystmJobItemProcessor();
    }
    @Bean
    public JpaItemWriter<JobDetail> jobManagementWriter() {
        logger.info("In Job Management Writer");
        JpaItemWriter<JobDetail> itr = new JpaItemWriter<JobDetail>();
        itr.setEntityManagerFactory(emf);
        return itr;

    }
    @Bean
    public ItemReader<JobDetail> jobManagementReader(DataSource dataSource) throws Exception {
        logger.info("In Job Management Reader");
        String QUERY_FIND_JOBS =
                "SELECT job from JobDetail job where job.status = 'QUEUED' AND job.executionDate <= now() ORDER BY job.priority ASC";

        JpaPagingItemReader<JobDetail> reader = new JpaPagingItemReader<JobDetail>();
        reader.setQueryString(QUERY_FIND_JOBS);
        reader.setEntityManagerFactory(emf);
        reader.setPageSize(3);
        reader.afterPropertiesSet();
        reader.setSaveState(true);
        return reader;
    }

    @Bean
    public JobMgmtSystmItemProcessorListener jobManagementItemProcessorlistener() {
        return  new JobMgmtSystmItemProcessorListener();
    }
    @Bean
    public Job simpleJob( ) throws Exception {
        return this.jobBuilderFactory
                .get("jobMgmtSystmJob")
                .incrementer(new RunIdIncrementer())
                .flow(jobManagementStep())
                .end()
                .build();
    }
}
