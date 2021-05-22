package com.jobManagement.config;

import com.jobManagement.helpers.JobDetailRowMapper;
import com.jobManagement.listener.CustomJobListener;
import com.jobManagement.listener.StepItemProcessListener;
import com.jobManagement.model.JobDetail;
import com.jobManagement.processor.FileUploadJobDetailProcessor;
import com.jobManagement.repository.JobDetailRepository;
import com.jobManagement.helpers.Constants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import java.io.IOException;
import java.net.MalformedURLException;

@Configuration
public class BatchConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    EntityManagerFactory emf;

    @Value("${file.upload1.dir}")
    private String dirName;
    JobDetailRepository jobDetailRepository;

    public static ByteArrayResource byteArrayResource = null;

    private final String JOB_QUERY ="select job.* from Job_Detail job where job.status = 'QUEUED' ORDER BY job.priority ASC";


    @Autowired
    private JpaItemWriter<JobDetail> writer;

    @Autowired
    private FlatFileItemReader<JobDetail> jobDetailItemReader;


    @Bean
    @StepScope
    public FlatFileItemReader<JobDetail> jobDetailItemReader(@Value("#{jobParameters['fileName']}") String filename) throws MalformedURLException {
        return new FlatFileItemReaderBuilder<JobDetail>()
                .name(Constants.ITEM_READER)
                .resource(new UrlResource(filename))
                .delimited()
                .names(new String[]{"name", "type","priority","execution_date","execute_immediate"})
                .lineMapper(lineMapper())
                .fieldSetMapper(new BeanWrapperFieldSetMapper<JobDetail>() {{
                    setTargetType(JobDetail.class);
                }})
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<JobDetail, JobDetail>chunk(10)
                .processor(processor())
                .writer(writer)
                .reader(jobDetailItemReader)
                .listener(new StepItemProcessListener())
                .build();
    }
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
    @Bean
    public LineMapper<JobDetail> lineMapper() {
        final DefaultLineMapper<JobDetail> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"name", "type","priority","execution_date","execute_immediate"});
        final JobDetailRowMapper fieldSetMapper = new JobDetailRowMapper();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }
    @Bean
    public FileUploadJobDetailProcessor processor()
    {
        return new FileUploadJobDetailProcessor();
    }
    @Bean
    @StepScope
    public JpaItemWriter<JobDetail> writer(@Value("#{stepExecutionContext['fileName']}") String filename,DataSource dataSource) throws IOException {
        JpaItemWriter<JobDetail> itr = new JpaItemWriter<JobDetail>();
        itr.setEntityManagerFactory(emf);
        return itr;

    }

    @Bean
    public Job job(CustomJobListener listener, Step step1) {
        return jobBuilderFactory.get(Constants.PROCESS_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

      /*
    This code was added to add partioning feature for parallel reads of files in the folder.

    @Bean("partitioner")
    @StepScope
    public Partitioner partitioner() {
        System.out.println("In Partitioner");
        MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources1 = null;
        try {
            resources1 = resolver.getResources(dirName+"/*.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        partitioner.setResources(resources1);
        partitioner.partition(10);
        return partitioner;
    }
    @Bean
    @Qualifier("masterStep")
    public Step masterStep() {
        return stepBuilderFactory.get("masterStep")
                .partitioner("step1", partitioner())
                .step(step1())
                .taskExecutor(taskExecutor())
                .build();
    }
    */

}
