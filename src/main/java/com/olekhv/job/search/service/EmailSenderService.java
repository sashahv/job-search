package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.config.EmailProperties;
import com.olekhv.job.search.entity.application.ApplicationStatus;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;

    public void sendEmail(ApplicationStatus applicationStatus,
                          UserCredential userCredential,
                          Job job,
                          Company company) {
        SimpleMailMessage message = new SimpleMailMessage();
        User sendToUser = userCredential.getUser();
        message.setTo(sendToUser.getContactEmail());

        String status = applicationStatus.toString().toLowerCase();
        EmailProperties.EmailMessage emailMessage = emailProperties.getEmailMessageMap().get(status);

        String subject = emailMessage.getSubject();
        String body = emailMessage.getBody();
        String formattedSubject = subject.replace("{COMPANY_NAME}", company.getName());
        message.setSubject(formattedSubject);
        String formattedBody = body
                .replace("{NAME}", sendToUser.getFirstName())
                .replace("{POSITION}", job.getRole())
                .replace("{COMPANY_NAME}", company.getName());
        message.setText(formattedBody);

        mailSender.send(message);
    }
}
