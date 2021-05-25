package com.jobProcessor.repository;

import com.jobProcessor.model.JobDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobDetailRepository extends CrudRepository<JobDetail, Long> {
    @Query(value = "select job from JobDetail job where job.status = 'QUEUED' ORDER BY job.priority ASC")
    Optional<List<JobDetail>> getQueuedJobs();
}
