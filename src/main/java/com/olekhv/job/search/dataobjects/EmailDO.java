package com.olekhv.job.search.dataobjects;

import lombok.Data;

@Data
public class EmailDO {
    private String toEmail;
    private String subject;
    private String body;
}
