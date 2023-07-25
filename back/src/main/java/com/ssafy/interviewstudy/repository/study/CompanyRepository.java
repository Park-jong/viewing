package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    public Optional<Company> findCompanyByName(String name);
}
