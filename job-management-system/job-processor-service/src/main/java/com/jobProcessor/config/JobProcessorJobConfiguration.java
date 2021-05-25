package com.jobProcessor.config;

import com.jobProcessor.listener.JobMgmtSystmItemProcessorListener;
import com.jobProcessor.processor.JobMgmtSystmJobItemProcessor;
import com.jobProcessor.model.JobDetail;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author Nivedita Singh
 * This class is used to configure the Batch process for processing jobs from database
 * based pon schedule and priority
 */

@Configuration
public class JobProcessorJobConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    EntityManagerFactory emf;
    @Autowired
    DataSource dataSource;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *  This is  a step in the batch execution job which defines the entire workflow of reading from database
     *  procesisng it and finally updating the status in the database.Multiple listeners can be configured
     *  here based on the requirement
     * @return
     * @throws Exception
     */
    @Bean
    public Step jobManagementStep() throws Exception {
        logger.info("In Job Management Step");
        return stepBuilderFactory
                .get("jobManagementStep")
                .<JobDetail, JobDetail>chunk(10)
                .reader(jobManagementReader(dataSource))
                .processor(jobManagementProcessor())
                .writer(jobManagementWriter())
                .listener(new JobMgmtSystmJobItemProcessor())
                .listener(new JobMgmtSystmItemProcessorListener())
                .build();
    }

    /**
     * This bean returns a processor for processing the job items (job detail records before sending it to the writer
     * @return
     */
    @Bean
    public ItemProcessor<JobDetail,JobDetail> jobManagementProcessor()  {
        logger.info("In Job Management Processor");
        return new JobMgmtSystmJobItemProcessor();
    }

    /**
     * This bean writes the records to the database
     * @return
     */
    @Bean
    public JpaItemWriter<JobDetail> jobManagementWriter() {
        logger.info("In Job Management Writer");
        JpaItemWriter<JobDetail> itr = new JpaItemWriter<JobDetail>();
        itr.setEntityManagerFactory(emf);
        return itr;

    }

    /**
     * This bean reads data from the database which would be processed later
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public ItemReader<JobDetail> jobManagementReader(DataSource dataSource) throws Exception {
        logger.info("In Job Management Reader");
        String QUERY_FIND_JOBS =
                "SELECT job from JobDetail job where job.status = 'QUEUED' AND (job.executionDate <= now() or job.executeImmediate=1 ) ORDER BY job.priority ASC";

        JpaPagingItemReader<JobDetail> reader = new JpaPagingItemReader<JobDetail>();
        reader.setQueryString(QUERY_FIND_JOBS);
        reader.setEntityManagerFactory(emf);
        reader.setPageSize(3);
        reader.afterPropertiesSet();
        reader.setSaveState(true);
        return reader;
    }

    /**
     * This bean returns a listener implementation of ItemProcessListener
     * @return
     */
    @Bean
    public JobMgmtSystmItemProcessorListener jobManagementItemProcessorlistener() {
        return  new JobMgmtSystmItemProcessorListener();
    }

    /**
     * This bean describes a step in the batch process
     * @return
     * @throws Exception
     */
    @Bean
    @Qualifier("jobMgmtSystmJob")
    public Job simpleJob( ) throws Exception {
        return this.jobBuilderFactory
                .get("jobMgmtSystmJob")
                .incrementer(new RunIdIncrementer())
                .flow(jobManagementStep())
                .end()
                .build();
    }
}
