package com.olekhv.job.search.datatransferobject;

import com.olekhv.job.search.entity.job.JobType;
import com.olekhv.job.search.entity.job.WorkType;
import com.olekhv.job.search.entity.skill.Skill;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class JobResponse {
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String country;
    private String city;
    private JobType type;
    private String role;
    private WorkType workType;
    private Integer emptyVacancies;
    private List<Skill> requiredSkills = new ArrayList<>();
    private Boolean isActive;
}
