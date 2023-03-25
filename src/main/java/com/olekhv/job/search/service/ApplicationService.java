package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.dataobjects.EmailDO;
import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.application.ApplicationStatus;
import com.olekhv.job.search.entity.application.Attachment;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.NoPermissionException;
import com.olekhv.job.search.exception.NotFoundException;
import com.olekhv.job.search.repository.ApplicationRepository;
import com.olekhv.job.search.repository.AttachmentRepository;
import com.olekhv.job.search.repository.CompanyRepository;
import com.olekhv.job.search.repository.JobRepository;
import com.olekhv.job.search.utils.AttachmentUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationService {
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final AttachmentRepository attachmentRepository;
    private final CompanyRepository companyRepository;
    private final EmailSenderService emailSenderService;

    public Application createNewApplication(UserCredential userCredential,
                                            Long jobId,
                                            List<MultipartFile> multipartFiles) {
        User authUser = userCredential.getUser();
        Job job = findJobById(jobId);
        Application application = new Application();
        application.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        application.setStatus(ApplicationStatus.APPLIED);
        application.setOwner(authUser);
        saveAttachmentsForApplication(multipartFiles, application);
        job.getApplications().add(application);
        jobRepository.save(job);
        return application;
    }

    // If user has next permissions:
    //         * he's recruiter
    //         * owns company
    //         * heads company
    // he can check all applications for certain job
    public List<Application> listAllApplications(Long jobId,
                                                 UserCredential userCredential){
        User authUser = userCredential.getUser();
        Job job = findJobById(jobId);
        Company company = findCompanyByJob(job);
        checkPermission(authUser, company);
        return job.getApplications();
    }

    public Application getApplicationById(Long applicationId,
                                            UserCredential userCredential){
        User authUser = userCredential.getUser();
        Application application = findApplicationById(applicationId);
        Job job = findJobByApplication(application);
        Company company = findCompanyByJob(job);
        if(!application.getOwner().equals(authUser)){
            checkPermission(authUser, company);
            application.setStatus(ApplicationStatus.VIEWED);
            applicationRepository.save(application);
        }
        return application;
    }

    public Attachment getAttachmentById(Long attachmentId,
                                          UserCredential userCredential) {
        User authUser = userCredential.getUser();
        Attachment attachment = getAttachmentById(attachmentId);
        Application application = findApplicationByAttachment(attachment);
        Job job = findJobByApplication(application);
        Company company = findCompanyByJob(job);
        if(!attachment.getOwner().equals(authUser)){
            checkPermission(authUser, company);
        }
        return attachment;
    }

    public Application changeApplicationStatus(Long applicationId,
                                               ApplicationStatus applicationStatus,
                                               UserCredential userCredential){
        User authUser = userCredential.getUser();
        Application application = findApplicationById(applicationId);
        Job job = findJobByApplication(application);
        Company company = findCompanyByJob(job);
        checkPermission(authUser, company);
        application.setStatus(applicationStatus);
        return applicationRepository.save(application);
    }

    public Application declineApplication(Long applicationId,
                                 EmailDO emailDO,
                                 UserCredential userCredential){
        User authUser = userCredential.getUser();
        Application application = findApplicationById(applicationId);
        Job job = findJobByApplication(application);
        Company company = findCompanyByJob(job);
        checkPermission(authUser, company);
        if(emailDO!=null){
            emailDO.setToEmail(application.getOwner().getContactEmail());
            emailSenderService.sendEmail(emailDO);
        }
        return changeApplicationStatus(applicationId, ApplicationStatus.DECLINED, userCredential);
    }

    // Deletes declined and closed applications if they exist one month
    // Checking is scheduled on 00:00 every day
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteInactiveApplications(){
        LocalDateTime monthAgo = LocalDateTime.now().minusDays(30).truncatedTo(ChronoUnit.SECONDS);
        List<Application> applicationsToDelete = generateListOfInactiveApplications(monthAgo);
        applicationRepository.deleteAll(applicationsToDelete);
        applicationsToDelete.forEach(application -> attachmentRepository.deleteAll(application.getAttachments()));
    }

    private List<Application> generateListOfInactiveApplications(LocalDateTime localDateTime) {
        List<Application> declinedApplications =
                applicationRepository.findByStatusAndCreatedAtBefore(ApplicationStatus.DECLINED, localDateTime);

        List<Application> closedApplications =
                applicationRepository.findByStatusAndCreatedAtBefore(ApplicationStatus.CLOSED, localDateTime);

        return Stream.concat(declinedApplications.stream(), closedApplications.stream())
                .collect(Collectors.toList());
    }

    // Check if user has next permissions:
    //     * he's recruiter
    //     * owns company
    //     * heads company
    // Otherwise, throws NoPermissionException
    private void checkPermission(User authUser, Company company) {
        if(!company.getOwner().equals(authUser)
                && !company.getHeads().contains(authUser)
                && !company.getHiringTeam().contains(authUser)){
            throw new NoPermissionException("No permission");
        }
    }

    private void saveAttachmentsForApplication(List<MultipartFile> multipartFiles, Application application) {
        List<Attachment> attachments = application.getAttachments();
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                Attachment attachment = Attachment.builder()
                        .name(multipartFile.getOriginalFilename())
                        .fileType(multipartFile.getContentType())
                        .data(AttachmentUtils.compressFile(multipartFile.getBytes()))
                        .owner(application.getOwner())
                        .build();
                attachments.add(attachment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        attachmentRepository.saveAll(attachments);
    }

    private Attachment getAttachmentById(Long attachmentId) {
        return attachmentRepository.findById(attachmentId).orElseThrow(
                () -> new NotFoundException("Attachment with id " + attachmentId + " not found")
        );
    }

    private Application findApplicationByAttachment(Attachment attachment) {
        return applicationRepository.findByAttachmentsContaining(attachment).orElseThrow(
                () -> new NotFoundException("Application with attachment id " + attachment.getId() + " not found")
        );
    }

    private Application findApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow(
                () -> new NotFoundException("Application with id " + applicationId + " not found")
        );
    }

    private Job findJobById(Long jobId) {
        return jobRepository.findById(jobId).orElseThrow(
                    () -> new NotFoundException("Job with id " + jobId + " not found")
            );
    }

    private Job findJobByApplication(Application application) {
        return jobRepository.findByApplicationsContaining(application).orElseThrow(
                () -> new NotFoundException("Job with application id " + application.getId() + " not found")
        );
    }

    private Company findCompanyByJob(Job job) {
        return companyRepository.findByJobsIsContaining(job).orElseThrow(
                () -> new NotFoundException("Company with job id " + job.getId() + " not found")
        );
    }
}
