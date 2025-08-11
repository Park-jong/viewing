package com.ssafy.interviewstudy.service.study.studyMember;

import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.study.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyMemberService {
    //스터디 가입 신청
    Integer addRequest(Integer studyId, RequestDto requestDto, List<MultipartFile> files);

    //스터디 가입 신청 조회
    List<RequestDtoResponse> findRequestsByStudy(Integer studyId);

    //스터디 가입 신청 개별 조회
    RequestDtoResponse findRequestById(Integer id);

    //스터디 신청 파일 다운로드
    RequestFile requestFileDownload(Integer studyId, Integer requestId, Integer fileId);

    //가입 신청 승인
    boolean permitRequest(Integer requestId, Integer studyId, Integer memberId);

    //가입 신청 거절
    void rejectRequest(Integer requestId, Integer studyId, Integer memberId);

    //가입 신청 취소
    void cancelRequest(Integer requestId, Integer studyId, Integer memberId);

    //스터디 탈퇴
    boolean leaveStudy(Integer studyId, Integer memberId);

    //스터디원 추방
    boolean banMemberStudy(Integer studyId, Integer memberId);

    //스터디장 위임
    void delegateLeader(Integer studyId, Integer leaderId, Integer memberId);

    //스터디원 목록 확인
    List<StudyMemberDto> findStudyMembers(Integer studyId);

    //스터디원인지 체크
    boolean checkStudyMember(Integer studyId, Integer memberId);

    //스터디의 스터디장인지 체크
    boolean checkStudyLeader(Integer studyId, Integer memberId);

    StudyMemberDto findStudyMember(Integer studyId, Integer memberId);

    //스터디 요청 유효성 체크 반환
    boolean checkStudyRequest(Integer requestId, Integer studyId, Integer memberId);
}
