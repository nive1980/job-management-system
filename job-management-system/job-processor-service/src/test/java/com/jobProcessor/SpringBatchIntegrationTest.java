package com.jobProcessor;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class SpringBatchIntegrationTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Test
    public void contextLoads()  throws Exception{

    }
    @Test
    public void job1_launch_should_success() throws Exception {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();

        JobExecution jobExec = jobLauncherTestUtils.launchJob(paramsBuilder.toJobParameters());
        assertThat(jobExec.getJobInstance().getJobName()).isEqualTo("jobMgmtSystmJob");
        assertThat(jobExec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    //junit totest a step
    /*@Test
    public void givenReferenceOutput_whenStep1Executed_thenSuccess() throws Exception {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("fileName","file:f/files/jobs1.csv");
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1", paramsBuilder.toJobParameters());
        Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        // then
        assertThat(actualStepExecutions.size()==1);
        Assert.assertEquals("COMPLETED", actualStepExecutions.stream().findFirst().get().getExitStatus().getExitCode());
    }*/



}