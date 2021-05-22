package com.jobManagement.task;

import com.fsit.clarien.recurring.model.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;

/*@Service
@Log4j2
@RequiredArgsConstructor*/
public class RecurringTransactionTask {

    @Autowired
    @Qualifier("jobMgmtSystmJob")
    private Job jobMgmtSystmJob;
    @Autowired
    private JobLauncher jobLauncher;
    //@Scheduled(cron = "${recurring.task.cron.expression}")
    public void processJobs() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("dateTime", new Date())
                .toJobParameters();

        jobLauncher.run(jobMgmtSystmJob, jobParameters);

    }



}
