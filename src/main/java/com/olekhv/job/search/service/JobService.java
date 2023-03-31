package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.datatransferobject.JobResponse;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.dataobject.JobDO;
import com.olekhv.job.search.entity.job.JobFilterFields;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.NoPermissionException;
import com.olekhv.job.search.exception.NotFoundException;
import com.olekhv.job.search.repository.CompanyRepository;
import com.olekhv.job.search.repository.JobRepository;
import com.olekhv.job.search.repository.UserRepository;
import com.olekhv.job.search.specification.JobSpec;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public Page<JobResponse> listAllJobs(Integer pageNumber,
                                 String sortField,
                                 String sortDirection,
                                 String keyword,
                                 JobFilterFields jobFilterFields) {
        log.info("jobFilterFields = " + jobFilterFields);
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        return generateJobPage(pageNumber, keyword, jobFilterFields, sort);
    }

    public Job createNewJob(JobDO jobDO,
                            Long companyId,
                            UserCredential userCredential) {
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
                             UserCredential userCredential) {
        User authUser = userCredential.getUser();
        Job job = findJobById(jobId);
        List<Job> authUserSavedJobs = authUser.getSavedJobs();
        authUserSavedJobs.add(job);
        userRepository.save(authUser);
        return authUserSavedJobs;
    }

    @Transactional
    public void deleteSavedJob(Long jobId,
                               UserCredential userCredential) {
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
    public void makeExpiredJobsInactive() {
        jobRepository.findAll().stream()
                .filter(this::isExpired)
                .forEach(job -> {
                    job.setIsActive(false);
                    jobRepository.save(job);
                });
    }

    public Job extendJobRecruitmentTerm(Long jobId,
                                        UserCredential userCredential) {
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
    public void deleteAllExpiredJobsAndApplications() {
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

    public boolean isExpired(Job job) {
        return job.getIsActive() && job.getExpiresAt().isBefore(LocalDateTime.now());
    }

    // Generate page with filtered jobs
    private PageImpl<JobResponse> generateJobPage(int pageNumber, String keyword, JobFilterFields jobFilterFields, Sort sort) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 1, sort);

        List<Job> jobs = generateJobList(keyword, jobFilterFields, pageable);

        List<JobResponse> jobResponses = jobs
                .stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(JobResponse::new)
                .toList();
        return new PageImpl<>(jobResponses, pageable, jobResponses.size());
    }

    // Generate list of jobs by filters
    private List<Job> generateJobList(String keyword, JobFilterFields jobFilterFields, Pageable pageable) {
        List<Job> jobs;
        if (keyword != null) {
            List<Job> jobsByKeyword = jobRepository.findAll(keyword);
            if (jobFilterFields != null) {
                jobs = getJobsByFilters(jobFilterFields)
                        .stream()
                        .filter(jobsByKeyword::contains)
                        .toList();
            } else {
                jobs = jobsByKeyword;
            }
        } else if (jobFilterFields != null) {
            jobs = getJobsByFilters(jobFilterFields);
        } else {
            jobs = jobRepository.findAll(pageable).getContent();
        }
        return jobs;
    }

    private List<Job> getJobsByFilters(JobFilterFields jobFilterFields) {
        if(jobFilterFields!=null){
            Specification<Job> jobSpecification = JobSpec.getSpec(
                    jobFilterFields.getCreatedAfterDaysAgo(),
                    jobFilterFields.getCountry(),
                    jobFilterFields.getCity(),
                    jobFilterFields.getJobType(),
                    jobFilterFields.getRole(),
                    jobFilterFields.getWorkType()
            );
            return jobRepository.findAll(jobSpecification);
        } else {
            return jobRepository.findAll();
        }
    }

    // Check if user has next permissions:
    //         * he's recruiter
    //         * owns company
    //         * heads company
    // Otherwise, throws NoPermissionException
    private void checkPermission(User authUser, Company company) {
        if (!company.getOwner().equals(authUser)
                && !company.getHeads().contains(authUser)
                && !company.getHiringTeam().contains(authUser)) {
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
