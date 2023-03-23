package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.application.ApplicationStatus;
import com.olekhv.job.search.entity.application.Attachment;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.dataobjects.JobDO;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.NoPermissionException;
import com.olekhv.job.search.exception.NotFoundException;
import com.olekhv.job.search.repository.CompanyRepository;
import com.olekhv.job.search.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    public Job createNewJob(JobDO jobDO,
                            Long companyId,
                            UserCredential userCredential){
        User authUser = userCredential.getUser();

        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new NotFoundException("Company with id " + companyId + " not found")
        );

        if(!company.getOwner().equals(authUser)
                && !company.getHeads().contains(authUser)
                && !company.getHiringTeam().contains(authUser)){
            throw new NoPermissionException("No permission");
        }

        Job job = buildJob(jobDO);

        company.getJobs().add(job);
        companyRepository.save(company);
        return jobRepository.save(job);
    }

    private Job buildJob(JobDO jobDO) {
        return Job.builder()
                .title(jobDO.getTitle())
                .description(jobDO.getDescription())
                .createdAt(LocalDateTime.now())
                .country(jobDO.getCountry())
                .city(jobDO.getCity())
                .type(jobDO.getType())
                .role(jobDO.getRole())
                .workType(jobDO.getWorkType())
                .emptyVacancies(jobDO.getEmptyVacancies())
                .requiredSkills(jobDO.getRequiredSkills())
                .build();
    }
}
