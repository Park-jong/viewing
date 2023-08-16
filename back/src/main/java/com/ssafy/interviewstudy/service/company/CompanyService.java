package com.ssafy.interviewstudy.service.company;

import com.ssafy.interviewstudy.repository.study.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public List<String> companyList(String name){
        return companyRepository.findCompanies("%"+name+"%");
    }
}
