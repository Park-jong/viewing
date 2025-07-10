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

    @Transactional
        //스터디 생성
    Integer addStudy(StudyDtoRequest studyDtoRequest);

    //스터디 삭제
    @Transactional
    void removeStudy(Integer studyId);

    //스터디 정보 수정
    @Transactional
    void modifyStudy(Integer studyId, StudyDtoRequest studyDtoRequest);

    //스터디 가입 신청
    @Transactional
    Integer addRequest(Integer studyId, RequestDto requestDto, List<MultipartFile> files);

    //스터디 가입 신청 조회
    List<RequestDtoResponse> findRequestsByStudy(Integer studyId);

    //스터디 가입 신청 개별 조회
    RequestDtoResponse findRequestById(Integer id);

    //스터디 신청 파일 다운로드
    RequestFile requestFileDownload(Integer studyId, Integer requestId, Integer fileId);

    //가입 신청 승인
    @Transactional
    boolean permitRequest(Integer requestId, Integer studyId, Integer memberId);

    //가입 신청 거절
    @Transactional
    void rejectRequest(Integer requestId, Integer studyId, Integer memberId);

    //가입 신청 취소
    @Transactional
    void cancelRequest(Integer requestId, Integer studyId, Integer memberId);

    //스터디 탈퇴
    @Transactional
    boolean leaveStudy(Integer studyId, Integer memberId);

    //스터디원 추방
    @Transactional
    boolean banMemberStudy(Integer studyId, Integer memberId);

    //스터디장 위임
    @Transactional
    void delegateLeader(Integer studyId, Integer leaderId, Integer memberId);

    //스터디원 목록 확인
    List<StudyMemberDto> findStudyMembers(Integer studyId);

    //스터디 실시간 채팅 작성
    @Transactional
    ChatResponse addChat(Integer studyId, ChatRequest chat);

    //스터디 실시간 채팅 조회
    List<ChatResponse> findStudyChats(Integer studyId, Integer lastChatId);

    //이전 채팅 보기
    List<ChatResponse> findOldStudyChats(Integer studyId, Integer startChatId);

    //스터디 일정 조회
    List<Object> findStudyCalendarsByStudy(Integer studyId);

    List<Object> findStudyCalendarsByMemberId(Integer memberId);

    List<Object> findStudyCalendarsByMemberIdStudyId(Integer memberId, Integer studyId);

    //일정 개별 조회
    StudyCalendarDtoResponse findStudyCalendarByStudy(Integer studyId, Integer calendarId);

    //스터디 일정 추가
    @Transactional
    Integer addStudyCalendar(Integer studyId, StudyCalendarDtoRequest studyCalendarDtoRequest);

    //스터디 일정 수정
    @Transactional
    void modifyStudyCalendar(Integer studyId, Integer studyCalendarId, StudyCalendarDtoRequest studyCalendarDtoRequest);

    //스터디 일정 삭제
    @Transactional
    void removeStudyCalendar(Integer studyId, Integer calendarId);

    //스터디원인지 체크
    boolean checkStudyMember(Integer studyId, Integer memberId);

    //스터디의 스터디장인지 체크
    boolean checkStudyLeader(Integer studyId, Integer memberId);

    StudyMemberDto findStudyMember(Integer studyId, Integer memberId);

    //스터디 요청 유효성 체크 반환
    boolean checkStudyRequest(Integer requestId, Integer studyId, Integer memberId);

    //스터디 유효성 체크
    boolean checkStudy(Integer studyId);
}
