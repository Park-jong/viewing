package com.ssafy.interviewstudy.service.study.studyMember;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.dto.notification.NotificationStudyDto;
import com.ssafy.interviewstudy.dto.study.*;
import com.ssafy.interviewstudy.exception.calendar.updateFailException;
import com.ssafy.interviewstudy.exception.member.MemberExceptionFactory;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.exception.study.StudyExceptionFactory;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.*;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import com.ssafy.interviewstudy.service.study.StudyFileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyMemberServiceImpl implements StudyMemberService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyRequestRepository studyRequestRepository;
    private final StudyRequestFileRepository studyRequestFileRepository;
    private final StudyChatRepository studyChatRepository;
    private final NotificationService notificationService;
    private final StudyFileManager studyFileManager;

    //스터디 가입 신청
    @Transactional
    @Override
    public Integer addRequest(Integer studyId, RequestDto requestDto, List<MultipartFile> files) {
        Optional<Study> studyOptional = studyRepository.findById(studyId);
        if(studyOptional.isEmpty() || studyOptional.get().getIsDelete()){//존재하지 않는 스터디
            return -3;
        }
        Study study = studyOptional.get();
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(MemberExceptionFactory::memberNotFound);
        Optional<StudyMember> isMember = studyMemberRepository.findByStudyAndMember(study, member);
        if(isMember.isPresent()){//이미 가입 되어 있음
            return -1;
        }
        Optional<StudyRequest> exist = studyRequestRepository.findStudyRequestByStudyAndApplicant(study, member);
        if(exist.isPresent()){//중복 신청 처리 거절(거절? 덮어쓰기?)
            return -2;
        }
        StudyRequest studyRequest = new StudyRequest(study, member, requestDto);
        studyRequestRepository.save(studyRequest);
        studyFileManager.saveFile(studyRequest, requestDto, files);
        //스터디 리더에게 스터디 신청 알림을 보내자!
        if(studyRequest.getId()!=null){
            notificationService.sendNotificationToMember(
                    NotificationDto.builder()
                            .notificationType(NotificationType.StudyRequest)
                            .content(study.getTitle()+" 스터디에 가입신청이 왔습니다.")
                            .memberId(study.getLeader().getId())
                            .url(study.getId().toString())
                            .isRead(false)
                            .build()
            );
        }

        return studyRequest.getId();
    }

    //스터디 가입 신청 조회
    @Override
    public List<RequestDtoResponse> findRequestsByStudy(Integer studyId){
        Study study = studyRepository.findById(studyId).orElseThrow(MemberExceptionFactory::memberNotFound);
        List<StudyRequest> requests = studyRequestRepository.findStudyRequestsByStudy(study);

        List<RequestDtoResponse> result = new ArrayList<>();

        for (StudyRequest request : requests) {
            StudyMemberDto user = new StudyMemberDto(request.getApplicant());
            List<StudyRequestFile> files = request.getStudyRequestFiles();
            List<RequestFile> reponseFiles = new ArrayList<>();
            for (StudyRequestFile file : files) {
                reponseFiles.add(new RequestFile(file));
            }
            RequestDtoResponse response = new RequestDtoResponse(request.getId(), user, request.getIntroduction(), request.getRequestedAt(), reponseFiles);
            result.add(response);
        }
        return result;
    }

    //스터디 가입 신청 개별 조회
    @Override
    public RequestDtoResponse findRequestById(Integer id){
        StudyRequest request = studyRequestRepository.findStudyRequestById(id).orElseThrow();
        List<StudyRequestFile> files = request.getStudyRequestFiles();
        List<RequestFile> reponseFiles = new ArrayList<>();
        for (StudyRequestFile file : files) {
            reponseFiles.add(new RequestFile(file));
        }
        return new RequestDtoResponse(request.getId(), new StudyMemberDto(request.getApplicant()), request.getIntroduction(), request.getRequestedAt(), reponseFiles);
    }

    //스터디 신청 파일 다운로드
    @Override
    public RequestFile requestFileDownload(Integer studyId, Integer requestId, Integer fileId){
        return studyFileManager.download(fileId);
    }

    //가입 신청 승인
    @Transactional
    @Override
    public boolean permitRequest(Integer requestId, Integer studyId, Integer memberId){
        StudyRequest studyRequest = checkRequest(requestId, studyId, memberId);
        Optional<Study> byStudyId = studyRepository.findById(studyId);
        if(byStudyId.isEmpty()) throw new NotFoundException("스터디를 찾을 수 없음");
        Study study = byStudyId.get();
        long count = studyMemberRepository.countStudyMemberByStudy(study);
        if(count >= study.getCapacity()) return false;

        deleteRequest(requestId);
        StudyMember sm = new StudyMember(studyRequest.getStudy(), studyRequest.getApplicant());
        sm.updateLeader(false);
        studyMemberRepository.save(sm);

        //스터디가 승인되었을때
        //승인된 멤버에게 알림을 보내자
        if(sm.getId()!=null){
            notificationService.sendNotificationToMember(
                    NotificationDto
                            .builder()
                            .memberId(memberId)
                            .content(studyRequest.getStudy().getTitle()+" 스터디에 가입이 승인되었습니다! ")
                            .notificationType(NotificationType.StudyRequest_Approve)
                            .url(studyId.toString())
                            .isRead(false)
                            .build()
            );
        }
        return true;
    }

    //유효한 요청인지 체크
    private StudyRequest checkRequest(Integer requestId, Integer studyId, Integer memberId){
        Optional<Study> studyOp = studyRepository.findById(studyId);
        Optional<Member> memberOp = memberRepository.findById(memberId);
        if(studyOp.isEmpty() || studyOp.get().getIsDelete() || memberOp.isEmpty()){
            return null;
        }

        Optional<StudyRequest> studyRequestOp = studyRequestRepository.findStudyAndMemberById(requestId);
        if(studyRequestOp.isEmpty() || studyRequestOp.get().getApplicant().getId() != memberId || studyRequestOp.get().getStudy().getId() != studyId){
            return null;
        }
        return studyRequestOp.get();
    }

    private void deleteRequest(Integer requestId){
        studyRequestFileRepository.deleteByRequestId(requestId);
        studyRequestRepository.deleteStudyRequestById(requestId);
    }

    //가입 신청 거절
    @Transactional
    @Override
    public void rejectRequest(Integer requestId, Integer studyId, Integer memberId){
        StudyRequest studyRequest = checkRequest(requestId, studyId, memberId);
        //deleteRequest전에 study를 가져오자
        Study study = studyRequest.getStudy();
        deleteRequest(requestId);

        //스터디가 거절되었을때
        notificationService.sendNotificationToMember(
              NotificationDto
                      .builder()
                      .content(study.getTitle()+" 스터디에 가입신청이 거절 되었습니다.")
                      .notificationType(NotificationType.StudyRequest_Reject)
                      .memberId(memberId)
                      .isRead(false)
                      .url(studyId.toString())
                      .build()
        );

    }

    //가입 신청 취소
    @Transactional
    @Override
    public void cancelRequest(Integer requestId, Integer studyId, Integer memberId){
        StudyRequest studyRequest = checkRequest(requestId, studyId, memberId);
        deleteRequest(requestId);
    }

    //스터디 탈퇴
    @Transactional
    @Override
    public boolean leaveStudy(Integer studyId, Integer memberId){
        Study study = studyRepository.findById(studyId).orElseThrow(StudyExceptionFactory::studyNotFound);
        if(Objects.equals(study.getLeader().getId(), memberId)){
            return false;
        }
        studyChatRepository.deleteChatMemberByStudyIdAndAuthorId(studyId, memberId);
        studyMemberRepository.deleteStudyMemberByStudyIdAndMemberId(studyId, memberId);
        return true;
    }

    //스터디원 추방
    @Transactional
    @Override
    public boolean banMemberStudy(Integer studyId, Integer memberId){
        Study study = studyRepository.findById(studyId).orElseThrow(StudyExceptionFactory::studyNotFound);
        if(Objects.equals(study.getLeader().getId(), memberId)){
            return false;
        }
        studyChatRepository.deleteChatMemberByStudyIdAndAuthorId(studyId, memberId);
        studyMemberRepository.deleteStudyMemberByStudyIdAndMemberId(studyId, memberId);

        //스터디에서 추방되었을때 추방된 당사자에게 알림
        notificationService.sendNotificationToMember(
                NotificationDto
                        .builder()
                        .content(study.getTitle()+" 스터디에서 추방되었습니다.")
                        .memberId(memberId)
                        .notificationType(NotificationType.Study_Banned)
                        .url(studyId.toString())
                        .isRead(false)
                        .build()
        );
        return true;
    }

    //스터디장 위임
    @Transactional
    @Override
    public void delegateLeader(Integer studyId, Integer leaderId, Integer memberId){
        StudyMember studyLeader,studyMember;
        try {
            if (leaderId == null || memberId == null || leaderId.equals(memberId)) throw new updateFailException("잘못된 요청");
            Study study = studyRepository.findById(studyId).orElseThrow(StudyExceptionFactory::studyNotFound);
            Member leader = study.getLeader();
            if (!Objects.equals(leader.getId(), leaderId)) throw new updateFailException("잘못된 요청");
            Member member = memberRepository.findById(memberId).orElseThrow(MemberExceptionFactory::memberNotFound);
            study.updateLeader(member);
            studyLeader = studyMemberRepository.findByStudyAndMember(study, leader).orElseThrow(StudyExceptionFactory::studyMemberNotFound);
            studyLeader.updateLeader(false);
            studyMember = studyMemberRepository.findByStudyAndMember(study, member).orElseThrow(StudyExceptionFactory::studyMemberNotFound);
            studyMember.updateLeader(true);
        }catch (NoSuchElementException ne){
            throw new updateFailException("잘못된 요청");
        }
        //리더 당사자에게 리더가 되었음을 알림
        notificationService.sendNotificationToMember(
                NotificationDto
                        .builder()
                        .notificationType(NotificationType.Leader)
                        .content(studyMember.getStudy().getTitle()+" 스터디의 리더가 되셨습니다!")
                        .memberId(memberId)
                        .url(studyId.toString())
                        .isRead(false)
                        .build()
        );

        //스터디원들에게 리더가 변경되었음을 알림
        notificationService.sendNotificationToStudyMember(
                NotificationStudyDto
                        .builder()
                        .notificationDto(
                                NotificationDto
                                        .builder()
                                        .notificationType(NotificationType.Leader)
                                        .content(studyMember.getStudy().getTitle()+" 스터디의 리더가 변경되었습니다.")
                                        .memberId(memberId)
                                        .url(studyId.toString())
                                        .build()
                        )
                        .studyId(studyId)
                        .build()
        );
    }

    //스터디원 목록 확인
    @Override
    public List<StudyMemberDto> findStudyMembers(Integer studyId){
        List<StudyMember> studyMembers = studyMemberRepository.findMembersByStudyId(studyId);
        List<StudyMemberDto> result = new ArrayList<>();
        for (StudyMember studyMember : studyMembers) {
            result.add(new StudyMemberDto(studyMember.getMember(), studyMember.getIsLeader()));
        }
        return result;
    }

    //스터디원인지 체크
    @Override
    public boolean checkStudyMember(Integer studyId, Integer memberId){
        Optional<StudyMember> result = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId);
        return result.isPresent();
    }

    //스터디의 스터디장인지 체크
    @Override
    public boolean checkStudyLeader(Integer studyId, Integer memberId){
        Optional<StudyMember> studyMemberOp = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId);
        if(studyMemberOp.isEmpty())
            return false;
        return studyMemberOp.get().getIsLeader();
    }

    @Override
    public StudyMemberDto findStudyMember(Integer studyId, Integer memberId){
        StudyMember studyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId).orElse(null);
        if(studyMember == null)
            return null;
        return new StudyMemberDto(studyMember.getMember(), studyMember.getIsLeader());
    }

    //스터디 요청 유효성 체크 반환
    @Override
    public boolean checkStudyRequest(Integer requestId, Integer studyId, Integer memberId) {
        return checkRequest(requestId, studyId, memberId) != null;
    }
}
