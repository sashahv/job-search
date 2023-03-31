package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.datatransferobject.UserResponse;
import com.olekhv.job.search.entity.application.Attachment;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.repository.AttachmentRepository;
import com.olekhv.job.search.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @Mock
    private User user;

    @Mock
    private UserCredential userCredential;
    @Mock
    private AttachmentRepository attachmentRepository;

    @BeforeEach
    void setUp() {
        when(userCredential.getUser()).thenReturn(user);
        when(userCredentialRepository.findByEmail("testEmail@gmail.com")).thenReturn(Optional.of(userCredential));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
    }

    @Test
    void should_show_user_response_when_fetch_user_by_id(){
        user.setId(1L);
        user.setFirstName("Test");

        UserResponse userResponse = userService.fetchUserById(1L);

        verify(userRepository,times(1)).findById(1L);
        assertEquals(userResponse.getId(), userResponse.getId());
        assertEquals(userResponse.getFirstName(), userResponse.getFirstName());
    }

    @Test
    void should_edit_user_information() {
        // Given
        User editedUser = new User();
        editedUser.setFirstName("ChangedName");

        // When
        userService.editInformation(editedUser, userCredential);

        // Then
        verify(user, times(1)).setFirstName("ChangedName");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void should_set_default_resume_for_user() throws IOException {
        MultipartFile file = new MockMultipartFile("file1", new byte[0]);

        Attachment attachment = userService.setDefaultResumeForUser(file, userCredential);

        verify(attachmentRepository, times(1)).save(any(Attachment.class));
        verify(userRepository,times(1)).save(any(User.class));
        assertEquals(user, attachment.getOwner());
    }
}