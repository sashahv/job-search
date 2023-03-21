package com.olekhv.job.search.model;

import com.olekhv.job.search.entity.user.User;
import lombok.Data;

@Data
public class CompanyModel {
    private String taxId;
    private String name;
    private String address;
    private String email;
    private User head;
}
