package com.ssafy.interviewstudy.service.study;

import com.ssafy.interviewstudy.domain.study.StudyRequest;
import com.ssafy.interviewstudy.dto.study.RequestDto;
import com.ssafy.interviewstudy.dto.study.RequestFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyFileManager {
    void saveFile(StudyRequest studyRequest, RequestDto requestDto, List<MultipartFile> files);

    RequestFile download(Integer fileId);
}
