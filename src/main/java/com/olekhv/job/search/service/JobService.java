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
import com.olekhv.job.search.repository.ApplicationRepository;
import com.olekhv.job.search.repository.CompanyRepository;
import com.olekhv.job.search.repository.JobRepository;
import com.olekhv.job.search.repository.UserRepository;
import jakarta.transaction.Transactional;
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
        jobRepository.save(job);
        return job;
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

    @Transactional
    public void deleteSavedJob(Long jobId,
                               UserCredential userCredential){
        User authUser = userCredential.getUser();
        Job job = findJobById(jobId);
        List<Job> authUserSavedJobs = authUser.getSavedJobs();
        if (authUserSavedJobs.contains(job)) {
            authUserSavedJobs.remove(job);
            userRepository.save(authUser);
        }
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

    public Job extendJobRecruitmentTerm(Long jobId,
                                        UserCredential userCredential){
        User authUser = userCredential.getUser();
        Job job = findJobById(jobId);
        Company company = findCompanyByJob(job);
        checkPermission(authUser, company);
        job.setExpiresAt(LocalDateTime.now().plusDays(30).truncatedTo(ChronoUnit.SECONDS));
        jobRepository.save(job);
        return job;
    }

    // Delete all jobs and applications of that job that were expired 90 days ago
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteAllExpiredJobsAndApplications(){
        jobRepository.findAll().stream()
                .filter(job -> !job.getIsActive())
                .filter(job -> job.getExpiresAt().isBefore(LocalDateTime.now().minusDays(90).truncatedTo(ChronoUnit.SECONDS)))
                .forEach(job -> deleteJob(job.getId()));
    }

    public void deleteJob(Long jobId) {
        Job job = findJobById(jobId);

        User user = findUserBySavedJob(job);
        user.getSavedJobs().remove(job);
        userRepository.save(user);

        Company company = findCompanyByJob(job);
        company.getJobs().remove(job);
        companyRepository.save(company);

        jobRepository.delete(job);
    }

    public boolean isExpired(Job job){
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

    private User findUserBySavedJob(Job job) {
        return userRepository.findBySavedJobsIsContaining(job).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    private Company findCompanyByJob(Job job) {
        return companyRepository.findByJobsIsContaining(job).orElseThrow(
                () -> new NotFoundException("Company with job id " + job.getId() + " not found")
        );
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
