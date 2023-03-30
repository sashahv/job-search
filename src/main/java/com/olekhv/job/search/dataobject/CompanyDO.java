package com.olekhv.job.search.dataobject;

import com.olekhv.job.search.entity.user.User;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDO {
    private String taxId;
    private String name;
    private String address;
    private String email;
    private List<User> heads;
}
