package com.ssafy.interviewstudy.dto.study;

import com.ssafy.interviewstudy.domain.study.CareerLevel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class StudyDtoRequest {
    private String title;

    private String description;

    private String appliedCompany;

    private String appliedJob;

    private int capacity;

    private boolean isRecruit;

    private LocalDateTime deadline;

    private Integer leaderId;

    private CareerLevel careerLevel;

    private List<Integer> tags = new ArrayList<>();
}
