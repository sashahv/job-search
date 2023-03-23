package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.dataobjects.JobDO;
import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.application.ApplicationStatus;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.repository.AttachmentRepository;
import com.olekhv.job.search.repository.CompanyRepository;
import com.olekhv.job.search.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ApplicationServiceTest {
    @InjectMocks private ApplicationService applicationService;

    @Mock private User user;
    @Mock private UserCredential userCredential;
    @Mock private AttachmentRepository attachmentRepository;
    @Mock private Job job;
    @Mock private JobRepository jobRepository;

    @BeforeEach
    void setUp() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userCredential.getUser()).thenReturn(user);
    }

    @Test
    void should_create_new_application() {
        MultipartFile file1 = new MockMultipartFile("file1", new byte[0]);
        MultipartFile file2 = new MockMultipartFile("file2", new byte[0]);
        List<MultipartFile> multipartFiles = Arrays.asList(file1, file2);
        when(job.getApplications()).thenReturn(new ArrayList<>());

        Application application = applicationService.createNewApplication(userCredential, 1L, multipartFiles);

        assertEquals(ApplicationStatus.APPLIED, application.getStatus());
        assertEquals(user, application.getOwner());
        assertEquals(2, application.getAttachments().size());
        assertEquals(1, job.getApplications().size());
        assertTrue(job.getApplications().contains(application));
        verify(jobRepository, times(1)).save(job);
        verify(attachmentRepository, times(1)).saveAll(application.getAttachments());
    }
}