package com.olekhv.job.search.datatransferobject;

import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.job.JobType;
import com.olekhv.job.search.entity.job.WorkType;
import com.olekhv.job.search.entity.skill.Skill;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class JobResponse {
    private Long id;
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

    public JobResponse(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.createdAt = job.getCreatedAt();
        this.expiresAt = job.getExpiresAt();
        this.country = job.getCountry();
        this.city = job.getCity();
        this.type = job.getType();
        this.role = job.getRole();
        this.workType = job.getWorkType();
        this.emptyVacancies = job.getEmptyVacancies();
        this.requiredSkills = job.getRequiredSkills();
        this.isActive = job.getIsActive();
    }
}
