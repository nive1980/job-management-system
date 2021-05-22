package com.jobManagement.helpers;

import com.jobManagement.model.JobDetail;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class JobDetailRowMapper implements FieldSetMapper<JobDetail> {

@Override
public JobDetail  mapFieldSet(FieldSet fieldSet) {
        JobDetail detail = new JobDetail();
        detail.setStatus("QUEUED");
        detail.setName(fieldSet.readString("name"));
        detail.setPriority(fieldSet.readInt("priority"));
        detail.setType(fieldSet.readString("type"));
        String isImm = fieldSet.readString("execute_immediate");
        detail.setExecuteImmediate((isImm != null && isImm.equals("1")) ? true:false);
        detail.setExecutionDate(fieldSet.readDate("execution_date"));
        return detail;
}
}
