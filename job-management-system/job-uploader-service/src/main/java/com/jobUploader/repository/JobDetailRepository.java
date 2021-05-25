package com.jobUploader.repository;

import com.jobUploader.model.JobDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobDetailRepository extends CrudRepository<JobDetail, Long> {
    @Query(value = "select job from JobDetail job where job.status = 'QUEUED' ORDER BY job.priority ASC")
    Optional<List<JobDetail>> getQueuedJobs();

    @Query(value = "select job from JobDetail job ORDER BY job.priority ASC")
    List<JobDetail> getAllJobs();
}
