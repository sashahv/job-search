package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.application.ApplicationStatus;
import com.olekhv.job.search.entity.application.Attachment;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.NoPermissionException;
import com.olekhv.job.search.repository.ApplicationRepository;
import com.olekhv.job.search.repository.AttachmentRepository;
import com.olekhv.job.search.repository.CompanyRepository;
import com.olekhv.job.search.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ApplicationServiceTest {
    @InjectMocks private ApplicationService applicationService;
    @Mock private JobService jobService;
    @Mock private User user;
    @Mock private UserCredential userCredential;
    @Mock private AttachmentRepository attachmentRepository;
    @Mock private Job job;
    @Mock private CompanyRepository companyRepository;
    @Mock private Company company;
    @Mock private JobRepository jobRepository;
    @Mock private ApplicationRepository applicationRepository;
    @Mock private Application application;
    @Mock private Attachment attachment;
    @Mock private EmailSenderService emailSenderService;

    @BeforeEach
    void setUp() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userCredential.getUser()).thenReturn(user);
        when(company.getOwner()).thenReturn(new User());
        when(application.getOwner()).thenReturn(user);
    }

    // If user belongs to hiring team he should be able to look at application by provide id.
    // If recruiter opens application he changes its status to "VIEWED"
    @Test
    void should_get_application_by_id_if_user_is_recruiter(){
        // Given
        when(company.getHiringTeam()).thenReturn(new ArrayList<>(Collections.singletonList(user)));
        Application newApplication = new Application();
        newApplication.setOwner(new User());
        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(newApplication));
        when(jobRepository.findByApplicationsContaining(newApplication)).thenReturn(Optional.of(job));
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));

        // When
        applicationService.getApplicationById(1L, userCredential);

        // Then
        verify(applicationRepository, times(1)).save(newApplication);
        assertEquals(ApplicationStatus.VIEWED, newApplication.getStatus());
    }

    // If user owns application he should be able to look at application by provide id.
    @Test
    void should_get_application_by_id_without_changes_if_user_owns_application(){
        // Given
        when(company.getHiringTeam()).thenReturn(new ArrayList<>(Collections.singletonList(new User())));
        Application newApplication = new Application();
        newApplication.setStatus(ApplicationStatus.APPLIED);
        newApplication.setOwner(user);
        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(newApplication));
        when(jobRepository.findByApplicationsContaining(newApplication)).thenReturn(Optional.of(job));
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));

        // When
        applicationService.getApplicationById(1L, userCredential);

        // Then
        assertEquals(ApplicationStatus.APPLIED, newApplication.getStatus());
    }

    // If user does not own application and does not have next permissions:
    //         * he's recruiter
    //         * owns company
    //         * heads company
    // He should not be able to look at other users' applications
    @Test
    @DisplayName("Throw exception if no permissions to get application by id")
    void should_throw_exception_if_try_to_get_application_by_id_without_permissions_and_owner_role(){
        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(application));
        when(jobRepository.findByApplicationsContaining(application)).thenReturn(Optional.of(job));
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));
        when(application.getOwner()).thenReturn(user);
        assertThrows(NoPermissionException.class, () ->
                applicationService.getApplicationById(1L, new UserCredential()));
    }


    @Test
    void should_create_new_application() {
        // Given
        MultipartFile file1 = new MockMultipartFile("file1", new byte[0]);
        MultipartFile file2 = new MockMultipartFile("file2", new byte[0]);
        List<MultipartFile> multipartFiles = Arrays.asList(file1, file2);
        when(job.getApplications()).thenReturn(new ArrayList<>());
        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(new Application()));
        when(companyRepository.findByJobsIsContaining(any(Job.class))).thenReturn(Optional.of(company));

        // When
        Application application = applicationService.createNewApplication(userCredential, 1L, multipartFiles);

        // Then
        assertEquals(ApplicationStatus.APPLIED, application.getStatus());
        assertEquals(user, application.getOwner());
        assertEquals(2, application.getAttachments().size());
        assertEquals(1, job.getApplications().size());
        assertTrue(job.getApplications().contains(application));
        verify(jobRepository, times(1)).save(job);
        verify(attachmentRepository, times(1)).saveAll(application.getAttachments());
    }

    @Test
    void should_throw_exception_if_create_job_and_job_proposition_is_expired(){
        when(job.getIsActive() && job.getExpiresAt().isBefore(LocalDateTime.now())).thenReturn(true);
        assertThrows(RuntimeException.class, () ->
                applicationService.createNewApplication(userCredential, 1L, null));
    }

    // If user has next permissions:
    //         * he's recruiter
    //         * owns company
    //         * heads company
    // he should be able to list all applications:
    @Test
    void should_list_all_applications_if_have_permission(){
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));
        when(company.getOwner()).thenReturn(user);
        when(job.getApplications()).thenReturn(Collections.singletonList(application));
        when(application.getOwner()).thenReturn(user);

        // When
        List<Application> applications = applicationService.listAllApplications(1L, userCredential);

        // Then
        assertEquals(1, applications.size());
    }

    // If user does not have next permissions:
    //         * he's recruiter
    //         * owns company
    //         * heads company
    // He should not be able to look at all applications to certain job
    @Test
    void should_throw_exception_when_try_to_list_all_applications_and_have_no_permission(){
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));

        assertThrows(NoPermissionException.class, () ->
                applicationService.listAllApplications(1L, new UserCredential()));
    }

    @Test
    void should_get_attachment_by_id(){
        when(attachmentRepository.findById(any(Long.class))).thenReturn(Optional.of(attachment));
        when(applicationRepository.findByAttachmentsContaining(attachment)).thenReturn(Optional.of(application));
        when(jobRepository.findByApplicationsContaining(application)).thenReturn(Optional.of(job));
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));
        when(attachment.getOwner()).thenReturn(user);

        applicationService.getAttachmentById(1L, userCredential);
    }

    @Test
    void should_change_application_status(){
        // Given
        Application newApplication = new Application();
        newApplication.setStatus(ApplicationStatus.VIEWED);
        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(newApplication));
        when(jobRepository.findByApplicationsContaining(newApplication)).thenReturn(Optional.of(job));
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));
        when(company.getHiringTeam()).thenReturn(new ArrayList<>(Collections.singletonList(user)));

        // When
        applicationService.changeApplicationStatus(newApplication, ApplicationStatus.CONSIDERED, userCredential);

        // Then
        assertEquals(ApplicationStatus.CONSIDERED, newApplication.getStatus());
    }

    // Application's status should change to "DECLINED" when recruited decline it
    @Test
    void should_change_application_status_to_declined(){
        // Given
        Application newApplication = new Application();
        newApplication.setStatus(ApplicationStatus.VIEWED);
        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(newApplication));
        when(jobRepository.findByApplicationsContaining(newApplication)).thenReturn(Optional.of(job));
        when(companyRepository.findByJobsIsContaining(job)).thenReturn(Optional.of(company));
        when(company.getHiringTeam()).thenReturn(new ArrayList<>(Collections.singletonList(user)));

        // When
        applicationService.declineApplication(1L, userCredential);

        // Then
        assertEquals(ApplicationStatus.DECLINED, newApplication.getStatus());
    }

    // Closed and declined applications should be deleted when they exist more than 30 days
    // with application status "DECLINED" or "CLOSED"
    @Test
    public void should_delete_closed_and_declined_applications() {
        // Given
        LocalDateTime monthAgo = LocalDateTime.now().minusDays(30).truncatedTo(ChronoUnit.SECONDS);
        List<Application> closedApplications = Collections.singletonList(application);
        List<Application> declinedApplications = Collections.singletonList(mock(Application.class));
        when(applicationRepository.findByStatusAndCreatedAtBefore(ApplicationStatus.CLOSED, monthAgo)).thenReturn(closedApplications);
        when(applicationRepository.findByStatusAndCreatedAtBefore(ApplicationStatus.DECLINED, monthAgo)).thenReturn(declinedApplications);
        when(application.getAttachments()).thenReturn(Collections.emptyList());
        when(declinedApplications.get(0).getAttachments()).thenReturn(Collections.emptyList());

        // When
        applicationService.deleteInactiveApplications();

        // Then
        verify(applicationRepository, times(1)).deleteAll(any());
        verify(attachmentRepository, times(2)).deleteAll(any());
    }
}