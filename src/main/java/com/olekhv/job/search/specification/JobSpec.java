package com.olekhv.job.search.specification;

import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.job.JobType;
import com.olekhv.job.search.entity.job.WorkType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobSpec {

    public static Specification<Job> getSpec(Integer createdAfterDaysAgo,
                                              String country,
                                              String city,
                                              JobType jobType,
                                              String role,
                                              WorkType workType){
        return ((root, query, criteriaBuilder) -> {
           List<Predicate> predicates = new ArrayList<>();
           if(createdAfterDaysAgo!=null){
               LocalDate createdAfterDate = LocalDate.now().minusDays(createdAfterDaysAgo);
               predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAfterDate));
           }
           if(country!=null && !country.isEmpty()){
               predicates.add(criteriaBuilder.equal(root.get("country"), country));
           }
           if(city!=null && !city.isEmpty()){
               predicates.add(criteriaBuilder.equal(root.get("city"), city));
           }
            if(jobType != null){
                predicates.add(criteriaBuilder.equal(root.get("type"), jobType));
            }
            if(role != null && !role.isEmpty()){
                predicates.add(criteriaBuilder.like(root.get("role"), "%" + role + "%"));
            }
            if(workType != null){
                predicates.add(criteriaBuilder.equal(root.get("workType"), workType));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
