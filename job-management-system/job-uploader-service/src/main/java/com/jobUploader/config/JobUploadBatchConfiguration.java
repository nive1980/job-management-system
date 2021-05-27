package com.jobUploader.config;

import com.jobUploader.helpers.Constants;
import com.jobUploader.helpers.FileVerificationSkipper;
import com.jobUploader.helpers.JobDetailRowMapper;
import com.jobUploader.helpers.JobDetailUploadPartitioner;
import com.jobUploader.listener.CustomJobListener;
import com.jobUploader.model.JobDetail;
import com.jobUploader.processor.FileUploadJobDetailProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @author  Nivedita Singh
 * This class is used to confgure the batch process for processing job details csv
 * files from a folder and upload it to a database table
 */

@Configuration
public class JobUploadBatchConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    EntityManagerFactory emf;

    @Value("${file.upload1.dir}")
    private String dirName;

    @Autowired
    private JpaItemWriter<JobDetail> writer;

    @Autowired
    private FlatFileItemReader<JobDetail> jobDetailItemReader;

    /**
     * This bean reads data from the csv file which would be processed later
     * @param filename
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * This bean defines the skip policy to be followed in case of error during file reading
     * @return
     */
    @Bean
    public SkipPolicy fileVerificationSkipper() {
        return new FileVerificationSkipper();
    }

    /**
     This is  a step in the batch execution job which defines the entire workflow of reading from csv file
     *  processing it and finally uploading it in the database.Multiple listeners can be configured
     *  here based on the requirement
     * @return
     */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<JobDetail, JobDetail>chunk(10)
                .processor(jobProcessor())
                .writer(writer)
                .reader(jobDetailItemReader)
                .faultTolerant()
                .skipPolicy(fileVerificationSkipper())
                .build();
    }

    /**
     * This bean defines the mapper which maps the line in csv file to the POJO
     * @return
     */
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

    /**
     * This bean returns a processor for processing the job items (CSV ITEM records before sending it to the writer
     * @return
     */
    @Bean
    public FileUploadJobDetailProcessor jobProcessor()
    {
        return new FileUploadJobDetailProcessor();
    }
    @Bean
    @StepScope
    @Primary
    public JpaItemWriter<JobDetail> jobWriter(@Value("#{stepExecutionContext['fileName']}") String filename,DataSource dataSource) throws IOException {
        JpaItemWriter<JobDetail> itr = new JpaItemWriter<JobDetail>();
        itr.setUsePersist(true);
        itr.setEntityManagerFactory(emf);
        return itr;

    }

    /**
     * This bean describes THE job in the batch process
     * @param listener
     * @param step1
     * @return
     */
    @Bean
    @Qualifier(Constants.PROCESS_JOB)
    public Job job(CustomJobListener listener, Step step1) {
        System.out.println(Constants.PROCESS_JOB+" job starting");
        return jobBuilderFactory.get(Constants.PROCESS_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

//This code is deliberately commented.
 /*
 This code was added to add partioning feature for parallel reads of files in the folder.


    @Bean("partitioner")
    @StepScope
    public JobDetailUploadPartitioner partitioner() {
        System.out.println("In Partitioner");
        JobDetailUploadPartitioner partitioner = new JobDetailUploadPartitioner();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = resolver.getResources(dirName+"/*.csv");
        } catch (IOException e) {
            throw new RuntimeException("I/O problems when resolving the input file pattern.", e);
        }
        partitioner.setResources(resources);
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
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
*/
}
