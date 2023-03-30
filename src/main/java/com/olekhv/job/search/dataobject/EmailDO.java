package com.olekhv.job.search.dataobject;

import com.olekhv.job.search.entity.application.ApplicationStatus;
import lombok.Data;

import java.util.List;

@Data
public class EmailDO {
    private List<ApplicationStatus> applicationStatuses;
    private String subject;
    private String body;
}
