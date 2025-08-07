package com.ssafy.interviewstudy.service.study;

import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.study.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyService {
    //내 스터디 조회
    List<StudyDtoResponse> findMyStudies(Integer id);

    //내가 찜한 스터디 조회
    List<StudyDtoResponse> findBookmarkStudies(Integer id);

    //스터디 정보 조회
    StudyDtoResponse findStudyById(JWTMemberInfo memberInfo, Integer id);

    //참가한 스터디 자세히보기
    StudyDetailDtoResponse findStudyDetailById(JWTMemberInfo memberInfo, Integer id);

    //스터디 검색 결과 조회
    Page<StudyDtoResponse> findStudiesBySearch(JWTMemberInfo memberInfo, Boolean option, String appliedCompany, String appliedJob, CareerLevel careerLevel, Integer tag, Pageable pageable);

    //스터디 생성
    Integer addStudy(StudyDtoRequest studyDtoRequest);

    //스터디 삭제
    void removeStudy(Integer studyId);

    //스터디 정보 수정
    void modifyStudy(Integer studyId, StudyDtoRequest studyDtoRequest);

    //스터디 유효성 체크
    boolean checkStudy(Integer studyId);
}
