package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.application.ApplicationStatus;
import com.olekhv.job.search.entity.application.Attachment;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.NotFoundException;
import com.olekhv.job.search.repository.ApplicationRepository;
import com.olekhv.job.search.repository.AttachmentRepository;
import com.olekhv.job.search.repository.JobRepository;
import com.olekhv.job.search.utils.AttachmentUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationService {
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final AttachmentRepository attachmentRepository;

    public Application fetchApplicationById(Long applicationId){
        return applicationRepository.findById(applicationId).orElseThrow(
                () -> new NotFoundException("Application with id " + applicationId + " not found")
        );
    }

    public Application createNewApplication(UserCredential userCredential,
                                            Long jobId,
                                            List<MultipartFile> multipartFiles) {
        User authUser = userCredential.getUser();

        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new NotFoundException("Job with id " + jobId + " not found")
        );

        Application application = new Application();
        application.setCreatedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.APPLIED);
        application.setOwner(authUser);
        saveAttachmentsForApplication(multipartFiles, application);
        job.getApplications().add(application);
        jobRepository.save(job);
        return application;
    }

    private void saveAttachmentsForApplication(List<MultipartFile> multipartFiles, Application application) {
        List<Attachment> attachments = application.getAttachments();
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                Attachment attachment = Attachment.builder()
                        .name(multipartFile.getOriginalFilename())
                        .fileType(multipartFile.getContentType())
                        .data(AttachmentUtils.compressFile(multipartFile.getBytes()))
                        .build();
                attachments.add(attachment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        attachmentRepository.saveAll(attachments);
    }
}
