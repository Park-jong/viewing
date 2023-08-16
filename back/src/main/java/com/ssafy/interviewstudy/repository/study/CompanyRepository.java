package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    public Optional<Company> findCompanyByName(String name);

    @Query("select c.name from Company c where c.name like :name")
    public List<String> findCompanies(String name);
}
