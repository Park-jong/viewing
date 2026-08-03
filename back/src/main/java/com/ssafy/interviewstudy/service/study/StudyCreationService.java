package com.ssafy.interviewstudy.service.study;

import com.ssafy.interviewstudy.dto.study.StudyDtoRequest;

public interface StudyCreationService {
    Integer addStudy(StudyDtoRequest studyDtoRequest);
}
