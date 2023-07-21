package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
