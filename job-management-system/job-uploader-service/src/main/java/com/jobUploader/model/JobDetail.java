package com.jobUploader.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * This class represents the job detail entity
 */
@Entity
@Getter @Setter @NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "JobDetail.gueued", query = "select job from JobDetail job where job.status = 'QUEUED' ORDER BY job.priority ASC")
})
public class JobDetail {
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID")
  @SequenceGenerator(
      sequenceName = "JOB_ID",
      allocationSize = 1,
      name = "JOB_ID")
  @Id
  @Column(name = "id")
  private Long jobId;

  @Column(name = "name")
  private String name;

  @Column(name = "type")
  private String type;

  @Column(name = "status")
  private String status;

  @Column(name = "priority")
  private int priority;

  @Column(name = "date_created")
  private Date dateCreated = new Date();

  @Column(name = "execution_date")
  private Date executionDate ;

  @Column(name = "execute_immediate")
  private boolean executeImmediate;


}
