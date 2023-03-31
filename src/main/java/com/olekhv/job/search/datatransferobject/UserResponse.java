package com.olekhv.job.search.datatransferobject;

import com.olekhv.job.search.entity.certificate.Certificate;
import com.olekhv.job.search.entity.education.Education;
import com.olekhv.job.search.entity.language.UserLanguage;
import com.olekhv.job.search.entity.skill.Skill;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.entity.workExperience.WorkExperience;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private LocalDate createdAt;
    private String firstName;
    private String lastName;
    private String currentPosition;
    private String contactEmail;
    private String phoneNumber;
    private LocalDate birthDate;
    private String country;
    private String city;
    private List<WorkExperience> workExperiences = new ArrayList<>();
    private List<Education> educations = new ArrayList<>();
    private List<Certificate> certificates = new ArrayList<>();
    private List<Skill> skills = new ArrayList<>();
    private List<UserLanguage> languages = new ArrayList<>();
    private List<User> connections = new ArrayList<>();

    public UserResponse(User user) {
        this.id = user.getId();
        this.createdAt = user.getCreatedAt();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.currentPosition = user.getCurrentPosition();
        this.contactEmail = user.getContactEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.birthDate = user.getBirthDate();
        this.country = user.getCountry();
        this.city = user.getCity();
        this.workExperiences = user.getWorkExperiences();
        this.educations = user.getEducations();
        this.certificates = user.getCertificates();
        this.skills = user.getSkills();
        this.languages = user.getLanguages();
        this.connections = user.getConnections();
    }
}
