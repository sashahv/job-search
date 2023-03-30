package com.olekhv.job.search.entity.job;

import lombok.Data;

@Data
public class JobFilterFields {
    private Integer createdAfterDaysAgo;
    private String country;
    private String city;
    private JobType jobType;
    private String role;
    private WorkType workType;
}
