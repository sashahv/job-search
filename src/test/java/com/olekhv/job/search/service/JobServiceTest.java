package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.dataobject.JobDO;
import com.olekhv.job.search.datatransferobject.JobResponse;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.job.JobFilterFields;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.NoPermissionException;
import com.olekhv.job.search.repository.CompanyRepository;
import com.olekhv.job.search.repository.JobRepository;
import com.olekhv.job.search.repository.UserRepository;
import org.hibernate.internal.util.collections.JoinedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JobServiceTest {
    @InjectMocks
    private JobService jobService;

    @Mock
    private User user;
    @Mock
    private UserCredential userCredential;
    @Mock
    private JobDO jobDO;
    @Mock
    private Job job;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private Company company;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        when(userCredential.getUser()).thenReturn(user);
        when(company.getOwner()).thenReturn(user);
        when(companyRepository.findById(any(Long.class))).thenReturn(Optional.of(company));
        when(jobRepository.findById(any(Long.class))).thenReturn(Optional.of(job));
    }

    @Test
    void should_create_new_job() {
        // Given
        when(company.getJobs()).thenReturn(new ArrayList<>());

        // When
        jobService.createNewJob(jobDO, 1L, userCredential);

        // Then
        verify(jobRepository, times(1)).save(any(Job.class));
        verify(companyRepository, times(1)).save(any(Company.class));
        assertEquals(1, company.getJobs().size());
    }

    // If user does not have next permissions:
    //         * he's recruiter
    //         * owns company
    //         * heads company
    // he should not be able to create new job
    @Test
    void should_throw_exception_if_no_permission() {
        assertThrows(NoPermissionException.class, () ->
                jobService.createNewJob(jobDO, 1L, new UserCredential()));
    }

    @Test
    void should_make_job_inactive_if_expired() {
        List<Job> expiredJobs = List.of(
                Job.builder().isActive(true).expiresAt(LocalDateTime.now().minusDays(1)).build(),
                Job.builder().isActive(true).expiresAt(LocalDateTime.now().plusDays(1)).build(),
                Job.builder().isActive(false).expiresAt(LocalDateTime.now().plusDays(1)).build()
        );
        when(jobRepository.findAll()).thenReturn(expiredJobs);

        jobService.makeExpiredJobsInactive();

        assertFalse(expiredJobs.get(0).getIsActive());
        assertTrue(expiredJobs.get(1).getIsActive());
        assertFalse(expiredJobs.get(2).getIsActive());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void should_extend_job_recruitment_termin() {
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));

        Job updatedJob = jobService.extendJobRecruitmentTerm(1L, userCredential);

        verify(jobRepository, times(1)).save(this.job);
        verify(this.job, times(1)).setExpiresAt(LocalDateTime.now().plusDays(30).truncatedTo(ChronoUnit.SECONDS));
        verify(jobRepository, times(1)).save(this.job);
        assertEquals(updatedJob, job);
    }

    @Test
    void should_delete_jobs_expired_90_days_ago() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusDays(91).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusDays(60).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime oneMonthLater = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Job> jobs = new ArrayList<>(List.of(
                Job.builder().id(1L).isActive(false).expiresAt(threeMonthsAgo).applications(new ArrayList<>()).build(),
                Job.builder().id(2L).isActive(false).expiresAt(twoMonthsAgo).applications(new ArrayList<>()).build(),
                Job.builder().id(3L).isActive(true).expiresAt(oneMonthLater).applications(new ArrayList<>()).build()
        ));
        when(jobRepository.findAll()).thenReturn(jobs);
        when(userRepository.findBySavedJobsIsContaining(any(Job.class))).thenReturn(Optional.of(user));
        when(companyRepository.findByJobsIsContaining(any(Job.class))).thenReturn(Optional.of(company));

        jobService.deleteAllExpiredJobsAndApplications();

        verify(jobRepository, times(1)).delete(any(Job.class));
    }

    @Test
    void should_delete_job() {
        when(userRepository.findBySavedJobsIsContaining(job)).thenReturn(Optional.of(user));
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));
        jobService.deleteJob(1L);

        verify(userRepository, times(1)).save(user);
        verify(companyRepository, times(1)).save(company);
        verify(jobRepository, times(1)).delete(job);
    }

    @Test
    void should_save_job() {
        // Given
        when(user.getSavedJobs()).thenReturn(new ArrayList<>());

        // When
        jobService.saveJob(1L, userCredential);

        // Then
        verify(userRepository, times(1)).save(user);
        assertEquals(1, user.getSavedJobs().size());
    }

    @Test
    void should_delete_saved_job() {
        // Given
        when(user.getSavedJobs()).thenReturn(new ArrayList<>(Collections.singletonList(job)));
        when(userRepository.findBySavedJobsIsContaining(job)).thenReturn(Optional.of(user));
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));

        // When
        jobService.deleteSavedJob(1L, userCredential);

        // Then
        verify(userRepository, times(1)).save(user);
        assertEquals(0, user.getSavedJobs().size());
    }

    @Test
    void should_return_page_with_size_1_and_one_job_by_keyword_and_specification() {
        // Given
        Integer pageNumber = 1;
        String sortField = "title";
        String sortDirection = "asc";
        String keyword = "developer";
        JobFilterFields jobFilterFields = new JobFilterFields();

        List<Job> jobs = Arrays.asList(
                new Job(),
                new Job()
        );

        when(jobRepository.findAll(keyword)).thenReturn(jobs);
        when(jobRepository.findAll(any(Specification.class))).thenReturn(jobs);

        // When
        Page<JobResponse> jobResponses = jobService.listAllJobs(pageNumber, sortField, sortDirection, keyword, jobFilterFields);

        // Then
        verify(jobRepository,times(1)).findAll(keyword);
        verify(jobRepository,times(1)).findAll(any(Specification.class));
        assertEquals(1, jobResponses.getTotalElements());
    }

    @Test
    void should_return_page_with_size_1_and_job_by_keyword() {
        // Given
        Integer pageNumber = 1;
        String sortField = "title";
        String sortDirection = "asc";
        String keyword = "developer";
        List<Job> jobs = new ArrayList<>(Collections.singleton(new Job()));

        when(jobRepository.findAll(keyword)).thenReturn(jobs);

        // When
        Page<JobResponse> jobResponses = jobService.listAllJobs(pageNumber, sortField, sortDirection, keyword, null);

        // Then
        verify(jobRepository,times(1)).findAll(keyword);
        verify(jobRepository,times(0)).findAll(any(Specification.class));
        assertEquals(1, jobResponses.getTotalElements());
    }

    @Test
    void should_return_page_with_size_1_and_job_without_filters() {
        // Given
        Integer pageNumber = 1;
        String sortField = "title";
        String sortDirection = "asc";
        String keyword = "developer";

        when(jobRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(new Job()))));

        // When
        Page<JobResponse> jobResponses =
                jobService.listAllJobs(pageNumber, sortField, sortDirection, null, null);

        // Then
        verify(jobRepository,times(0)).findAll(keyword);
        verify(jobRepository,times(1)).findAll(any(Pageable.class));
        verify(jobRepository,times(0)).findAll(any(Specification.class));
        assertEquals(1, jobResponses.getTotalElements());
    }
}