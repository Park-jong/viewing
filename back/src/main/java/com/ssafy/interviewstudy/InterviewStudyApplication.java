package com.ssafy.interviewstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InterviewStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewStudyApplication.class, args);
    }

}
