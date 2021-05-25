package com.jobUploader.service;

import com.jobUploader.model.JobDetail;
import com.jobUploader.repository.JobDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class represnts the service to retrieve job details from the database
 */
@Component
@Service
@RequiredArgsConstructor
public class JobDetailService {
    private final JobDetailRepository jobDetailRepository;
    public Object getJobDetails() {
        List<JobDetail> jobDetailList =  jobDetailRepository.getAllJobs();
        return jobDetailList;
    }
}
