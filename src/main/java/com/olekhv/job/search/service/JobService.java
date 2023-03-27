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
import com.olekhv.job.search.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public Job createNewJob(JobDO jobDO,
                            Long companyId,
                            UserCredential userCredential){
        User authUser = userCredential.getUser();
        Company company = findCompanyById(companyId);
        checkPermission(authUser, company);
        Job job = buildJob(jobDO);
        company.getJobs().add(job);
        companyRepository.save(company);
        return jobRepository.save(job);
    }

    public List<Job> saveJob(Long jobId,
                             UserCredential userCredential){
        User authUser = userCredential.getUser();
        Job job = findJobById(jobId);
        List<Job> authUserSavedJobs = authUser.getSavedJobs();
        authUserSavedJobs.add(job);
        userRepository.save(authUser);
        return authUserSavedJobs;
    }

    public List<Job> deleteSavedJob(Long jobId,
                                    UserCredential userCredential){
        User authUser = userCredential.getUser();
        Job job = findJobById(jobId);
        List<Job> authUserSavedJobs = authUser.getSavedJobs();
        authUserSavedJobs.remove(job);
        userRepository.save(authUser);
        return authUserSavedJobs;
    }

    // Every night at 00:00 system will check
    // if jobs are not expired. If some job expires
    // it will get "Expired" status
    @Scheduled(cron = "0 0 0 * * *")
    public void makeExpiredJobsInactive(){
        jobRepository.findAll().stream()
                .filter(this::isExpired)
                .forEach(job -> {
                    job.setIsActive(false);
                    jobRepository.save(job);
                });
    }

    private boolean isExpired(Job job){
        return job.getIsActive() && job.getExpiresAt().isBefore(LocalDateTime.now());
    }

    // Check if user has next permissions:
    //         * he's recruiter
    //         * owns company
    //         * heads company
    // Otherwise, throws NoPermissionException
    private void checkPermission(User authUser, Company company) {
        if(!company.getOwner().equals(authUser)
                && !company.getHeads().contains(authUser)
                && !company.getHiringTeam().contains(authUser)){
            throw new NoPermissionException("No permission");
        }
    }

    private Job buildJob(JobDO jobDO) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return Job.builder()
                .isActive(true)
                .title(jobDO.getTitle())
                .description(jobDO.getDescription())
                .createdAt(now)
                .expiresAt(now.plusDays(30))
                .country(jobDO.getCountry())
                .city(jobDO.getCity())
                .type(jobDO.getType())
                .role(jobDO.getRole())
                .workType(jobDO.getWorkType())
                .emptyVacancies(jobDO.getEmptyVacancies())
                .requiredSkills(jobDO.getRequiredSkills())
                .build();
    }

    private Company findCompanyById(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(
                () -> new NotFoundException("Company with id " + companyId + " not found")
        );
    }

    private Job findJobById(Long jobId) {
        return jobRepository.findById(jobId).orElseThrow(
                () -> new NotFoundException("Job with id " + jobId + " not found")
        );
    }
}
