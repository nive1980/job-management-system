package com.jobUploader.scheduler;


import com.jobUploader.helpers.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.*;
import java.util.Date;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * This class is used the schedule the File upload job using the cron expression
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class JobUploadToDBTaskScheduler {

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    TaskExecutor taskExecutor;

    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    @Qualifier(Constants.PROCESS_JOB)
    private Job job;

    @Value("${file.upload.dir}")
    private String uploadDir;

    Logger logger = LoggerFactory.getLogger(this.getClass());
     static boolean initialFilesRead = false;
  @Scheduled(cron = "${recurring.task.cron.expression}")
    public void searchFiles() throws Exception {
      if(!initialFilesRead) {
          initialFilesRead = true;
          File direcotry = new File(uploadDir);
          if (direcotry.isDirectory()) {
              String contents[] = direcotry.list();
              for (String name : contents) {
                  System.out.println("contents " + name);
                  String fileNameToPass = "file:" + uploadDir + name;
                  logger.info("fileName:  " + fileNameToPass);

                  JobParameters jobParameters = new JobParametersBuilder()
                          .addString("fileName", fileNameToPass)
                          .addDate("dateTime", new Date())
                          .toJobParameters();

                  jobLauncher.run(job, jobParameters);

              }
          }
      }
     WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(uploadDir);

        dir.register(watcher, ENTRY_CREATE);
        logger.info("Watch Service registered for dir: " + dir.getFileName());
        WatchKey key; while ((key = watcher.take())!=null)
        {
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path fileName = ev.context();

                if(kind==ENTRY_CREATE)
                {
                    if(fileName.toString().toLowerCase().endsWith(".csv")) {
                        String fileNameToPass = "file:"+uploadDir+fileName;
                        logger.info("fileName:  " +fileNameToPass);

                        JobParameters jobParameters = new JobParametersBuilder()
                                .addString("fileName", fileNameToPass)
                                .addDate("dateTime", new Date())
                                .toJobParameters();

                        jobLauncher.run(job, jobParameters);

                    }
                }
            }
        boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
  protected JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.setMaxVarCharLength(10000);
        return factory.getObject();
    }


}
