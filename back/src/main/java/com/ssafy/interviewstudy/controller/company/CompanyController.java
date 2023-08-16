package com.ssafy.interviewstudy.controller.company;

import com.ssafy.interviewstudy.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = {"/companies"})
@RestController
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> CompanyList(@PathVariable("name") String name) {
        return ResponseEntity.ok().body(companyService.companyList(name));
    }
}
