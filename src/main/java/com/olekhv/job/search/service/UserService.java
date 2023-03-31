package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.datatransferobject.UserResponse;
import com.olekhv.job.search.entity.application.Attachment;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.entity.user.UserRole;
import com.olekhv.job.search.repository.AttachmentRepository;
import com.olekhv.job.search.repository.UserRepository;
import com.olekhv.job.search.utils.AttachmentUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.FileUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserCredentialRepository userCredentialRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;

    public UserResponse fetchUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User with id " + id + " not found")
        );

        return new UserResponse(user);
    }

    @Transactional
    public User editInformation(User editedUser,
                                UserCredential userCredential) {
        User authUser = userCredential.getUser();
        authUser.setFirstName(editedUser.getFirstName());
        authUser.setLastName(editedUser.getLastName());
        authUser.setPhoneNumber(editedUser.getPhoneNumber());
        authUser.setCurrentPosition(editedUser.getCurrentPosition());
        authUser.setContactEmail(editedUser.getContactEmail() != null ? editedUser.getContactEmail() : userCredential.getEmail());
        authUser.setCountry(editedUser.getCountry());
        authUser.setCity(editedUser.getCity());
        userRepository.save(authUser);
        return authUser;
    }

    public Attachment setDefaultResumeForUser(MultipartFile multipartFile,
                                        UserCredential userCredential) throws IOException {
        User authUser = userCredential.getUser();
        Attachment attachment = Attachment.builder()
                .name(multipartFile.getOriginalFilename())
                .fileType(multipartFile.getContentType())
                .owner(authUser)
                .data(AttachmentUtils.compressFile(multipartFile.getBytes()))
                .build();
        attachmentRepository.save(attachment);
        authUser.setDefaultResume(attachment);
        userRepository.save(authUser);
        return attachment;
    }

    public User upgradeToPremium(UserCredential userCredential){
        userCredential.setRole(UserRole.PREMIUM_USER);
        return userCredentialRepository.save(userCredential).getUser();
    }
}
