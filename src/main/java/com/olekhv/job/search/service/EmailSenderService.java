package com.olekhv.job.search.service;

import com.olekhv.job.search.dataobjects.EmailDO;
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

    public void sendEmail(EmailDO emailDO){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDO.getToEmail());
        message.setSubject(emailDO.getSubject());
        message.setText(emailDO.getBody());
        mailSender.send(message);
    }
}
