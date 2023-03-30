package com.olekhv.job.search.dataobject;

import com.olekhv.job.search.entity.job.JobType;
import com.olekhv.job.search.entity.job.WorkType;
import com.olekhv.job.search.entity.skill.Skill;
import lombok.Data;

import java.util.List;

@Data
public class JobDO {
    private String title;
    private String description;
    private String country;
    private String city;
    private JobType type;
    private String role;
    private WorkType workType;
    private Integer emptyVacancies;
    private List<Skill> requiredSkills;
}
