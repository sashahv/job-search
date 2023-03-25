package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.auth.userCredential.UserCredentialRepository;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.AlreadyExistsException;
import com.olekhv.job.search.exception.NoPermissionException;
import com.olekhv.job.search.exception.NotFoundException;
import com.olekhv.job.search.dataobjects.CompanyDO;
import com.olekhv.job.search.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserCredentialRepository userCredentialRepository;

    public Company createNewCompany(CompanyDO companyDO,
                                    UserCredential userCredential) {
        User authUser = userCredential.getUser();
        if (companyRepository.findByName(companyDO.getName()).isPresent()) {
            throw new AlreadyExistsException("Company with name \"" + companyDO.getName() + "\" already exists");
        }
        Company company = buildCompany(companyDO, authUser);
        return companyRepository.save(company);
    }

    public List<User> addUserToHiringTeam(String userEmail,
                                          Long companyId,
                                          UserCredential userCredential){
        User authUser = userCredential.getUser();
        Company company = findCompanyById(companyId);
        if(!company.getOwner().equals(authUser) && !company.getHeads().contains(authUser)){
            throw new NoPermissionException("No permission");
        }
        UserCredential providedUserCredential = findUserCredentialByEmail(userEmail);
        User providedUser = providedUserCredential.getUser();
        List<User> companyHiringTeam = company.getHiringTeam();
        companyHiringTeam.add(providedUser);
        companyRepository.save(company);
        return companyHiringTeam;
    }

    private Company buildCompany(CompanyDO companyDO, User authUser) {
        return Company
                .builder()
                .name(companyDO.getName())
                .taxId(companyDO.getTaxId())
                .email(companyDO.getEmail())
                .address(companyDO.getAddress())
                .owner(authUser)
                .heads(companyDO.getHeads())
                .build();
    }

    private UserCredential findUserCredentialByEmail(String userEmail) {
        return userCredentialRepository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("User " + userEmail + " does not exist")
        );
    }

    public Company findCompanyById(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(
                () -> new NotFoundException("Company with id " + companyId + " not found")
        );
    }
}
