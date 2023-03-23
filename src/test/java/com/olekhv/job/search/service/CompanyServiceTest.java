package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.AlreadyExistsException;
import com.olekhv.job.search.exception.NoPermissionException;
import com.olekhv.job.search.dataobjects.CompanyDO;
import com.olekhv.job.search.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CompanyServiceTest {
    @InjectMocks private CompanyService companyService;

    @Mock private CompanyRepository companyRepository;
    @Mock private UserCredentialRepository userCredentialRepository;
    @Mock private Company company;
    @Mock private User user;
    @Mock private UserCredential userCredential;

    @BeforeEach
    void setUp() {
        when(userCredential.getUser()).thenReturn(user);
        when(userCredentialRepository.findByEmail("testEmail@gmail.com")).thenReturn(Optional.of(userCredential));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(company.getHiringTeam()).thenReturn(new ArrayList<>());
        when(company.getOwner()).thenReturn(user);
    }

    @Test
    void should_create_new_company(){
        // Given
        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        // When
        companyService.createNewCompany(new CompanyDO(), userCredential);

        // Then
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    void should_throw_exception_if_company_already_exists(){
        CompanyDO companyDO = new CompanyDO();
        companyDO.setName("Test Company");
        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.of(company));

        assertThrows(AlreadyExistsException.class, () ->
                companyService.createNewCompany(companyDO, userCredential));
    }

    @Test
    void should_add_user_to_hiring_team(){
        // Given
        User providedUser = mock(User.class);
        UserCredential providedUserCredential = mock(UserCredential.class);
        when(providedUserCredential.getUser()).thenReturn(providedUser);
        when(userCredentialRepository.findByEmail("testProvidedEmail@gmail.com")).thenReturn(Optional.of(providedUserCredential));
        // When
        companyService.addUserToHiringTeam("testProvidedEmail@gmail.com", 1L, userCredential);

        // Then
        verify(companyRepository, times(1)).save(company);
        assertThat(company.getHiringTeam().contains(providedUser)).isTrue();
    }

    @Test
    void should_throw_exception_if_no_permission(){
        UserCredential providedUserCredential = mock(UserCredential.class);
        User providedUser = mock(User.class);
        when(providedUserCredential.getUser()).thenReturn(providedUser);
        when(company.getOwner()).thenReturn(user);

        assertThrows(NoPermissionException.class, () ->
                companyService.addUserToHiringTeam("testEmail@gmail.com", 1L, providedUserCredential));
    }
}