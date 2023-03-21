package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.AlreadyExistsException;
import com.olekhv.job.search.exception.NoPermissionException;
import com.olekhv.job.search.exception.NotFoundException;
import com.olekhv.job.search.model.CompanyModel;
import com.olekhv.job.search.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserCredentialRepository userCredentialRepository;

    public Company createNewCompany(CompanyModel companyModel,
                                    UserCredential userCredential) {
        User authUser = userCredential.getUser();

        if (companyRepository.findByName(companyModel.getName()).isPresent()) {
            throw new AlreadyExistsException("Company with name \"" + companyModel.getName() + "\" already exists");
        }

        Company company = Company
                .builder()
                .name(companyModel.getName())
                .taxId(companyModel.getTaxId())
                .email(companyModel.getEmail())
                .address(companyModel.getAddress())
                .owner(authUser)
                .heads(Collections.singletonList(companyModel.getHead()))
                .build();

        return companyRepository.save(company);
    }

    public Company addUserToHiringTeam(String userEmail,
                                       Long companyId,
                                       UserCredential userCredential){
        User authUser = userCredential.getUser();

        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new NotFoundException("Company with id " + companyId + " not found")
        );

        if(!company.getOwner().equals(authUser) && !company.getHeads().contains(authUser)){
            throw new NoPermissionException("No permission");
        }

        UserCredential providedUserCredential = userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("User " + userEmail + " does not exist")
        );

        User providedUser = providedUserCredential.getUser();

        company.getHiringTeam().add(providedUser);
        return companyRepository.save(company);
    }
}
