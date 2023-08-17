package com.ssafy.interviewstudy.service.study;

import com.querydsl.core.Tuple;
import com.ssafy.interviewstudy.domain.calendar.Calendar;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.notification.Notification;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.dto.calendar.CalendarListResponse;
import com.ssafy.interviewstudy.dto.calendar.CalendarRetrieveResponse;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.dto.notification.NotificationStudyDto;
import com.ssafy.interviewstudy.dto.study.*;
import com.ssafy.interviewstudy.exception.calendar.updateFailException;
import com.ssafy.interviewstudy.exception.message.CreationFailException;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.calendar.CalendarRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.*;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import com.ssafy.interviewstudy.support.file.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final CompanyRepository companyRepository;
    private final StudyTagRepository studyTagRepository;
    private final StudyTagTypeRepository studyTagTypeRepository;
    private final StudyRequestRepository studyRequestRepository;
    private final StudyRequestFileRepository studyRequestFileRepository;
    private final StudyChatRepository studyChatRepository;
    private final StudyCalendarRepository studyCalendarRepository;
    private final StudyBookmarkRepository studyBookmarkRepository;
    private final NotificationService notificationService;
    private final CalendarRepository calendarRepository;
    private  final FileManager fm;

    //내 스터디 조회
    public List<StudyDtoResponse> findMyStudies(Integer id){
        Member member = memberRepository.findById(id).get();
        //태그들 가져옴
        List<Study> studies = studyRepository.findStudiesByMember(memberRepository.findById(id).get());
        //멤버수 가져옴(스터디, 멤버수)
        List<Tuple> counts = studyRepository.findMyStudyMemberCountByMember(member);
        List<StudyDtoResponse> result = new ArrayList<>();
        for (Tuple tuple : counts) {
            result.add(new StudyDtoResponse(tuple.get(0, Study.class), tuple.get(1, Boolean.class), tuple.get(2, Long.class)));
        }
        return result;
    }

    //내가 찜한 스터디 조회
    public List<StudyDtoResponse> findBookmarkStudies(Integer id){
        Member member = memberRepository.findById(id).get();
        //1차 캐시에 올림(태그들)
        List<Study> studies = studyRepository.findBookmarksByMember(member);
        //(Study, 멤버수)
        List<Tuple> counts = studyRepository.findBookmarksMemberCountByMember(member);
        List<StudyDtoResponse> result = new ArrayList<>();
        for (Tuple tuple : counts) {
            result.add(new StudyDtoResponse(tuple.get(0, Study.class), true, tuple.get(1, Long.class)));
        }
        return result;
    }

    //스터디 정보 조회
    public StudyDtoResponse findStudyById(JWTMemberInfo memberInfo, Integer id){
        Integer memberId = memberInfo.getMemberId();
        Study study = studyRepository.findStudyById(id);
        checkExist(study);
        long headCount = studyMemberRepository.countStudyMemberByStudy(study);
        StudyBookmark sb = studyBookmarkRepository.findStudyBookmarkByStudyIdAndMemberId(id, memberId);
        return new StudyDtoResponse(study, sb != null, headCount);
    }

    //참가한 스터디 자세히보기
    public StudyDetailDtoResponse findStudyDetailById(JWTMemberInfo memberInfo, Integer id){
        Integer memberId = memberInfo.getMemberId();
        Study study = studyRepository.findStudyById(id);
        checkExist(study);
        studyMemberRepository.findMembersByStudy(study);
        return new StudyDetailDtoResponse(study);
    }

    //스터디 검색 결과 조회
    public Page<StudyDtoResponse> findStudiesBySearch(JWTMemberInfo memberInfo, Boolean option, String appliedCompany, String appliedJob, CareerLevel careerLevel, Integer tag, Pageable pageable){
        Integer memberId = memberInfo != null ? memberInfo.getMemberId() : null;
        //검색 결과 (study_id, Study, 북마크여부, 인원)
        Page<Tuple> studies = studyRepository.findStudiesBySearch(option, appliedCompany, appliedJob, careerLevel, tag, pageable);
        List<StudyDtoResponse> result = new ArrayList<>();
        List<Integer> studyids = new ArrayList<>();
        for (Tuple study : studies) {
            studyids.add(study.get(0, Integer.class));
        }
        //태그들을 가져옴
        List<Study> byIds = studyRepository.findByIds(studyids);
        List<Tuple> etc = studyRepository.isBookmark(memberId, studyids);
        List<Tuple> content = studies.getContent();
        for (int i=0, size = content.size(); i<size; i++) {
            result.add(new StudyDtoResponse(content.get(i).get(1, Study.class), etc.get(i).get(0, Boolean.class), etc.get(i).get(1, Long.class)));
        }
        return new PageImpl<>(result, pageable, studies.getTotalElements());
    }


    @Transactional
    //스터디 생성
    public Integer addStudy(StudyDtoRequest studyDtoRequest){
        Study study = requestToStudy(studyDtoRequest);
        Member leader = memberRepository.findById(studyDtoRequest.getLeaderId()).get();
        study.updateLeader(leader);
        Optional<Company> companyByName = companyRepository.findCompanyByName(studyDtoRequest.getAppliedCompany());
        if(companyByName.isEmpty()) throw new NotFoundException("해당 정보를 찾을 수 없음");
        Company company = companyByName.get();
        study.updateCompany(company);
        studyRepository.save(study);
        //태그들 추가
        List<Integer> tags = studyDtoRequest.getTags();
        for (Integer tag : tags) {
            StudyTagType stt = studyTagTypeRepository.findById(tag).get();
            StudyTag st = new StudyTag(study, stt);
            studyTagRepository.save(st);
        }

        StudyMember studyMember = new StudyMember(study, leader);
        studyMember.updateLeader(true);
        studyMemberRepository.save(studyMember);

        return study.getId();
    }

    //스터디 삭제
    @Transactional
    public void removeStudy(Integer studyId){
        Study study = studyRepository.findById(studyId).get();
        study.updateLeader(null);
        study.deleteStudy();
        studyMemberRepository.deleteStudyMemberByStudy(study);
    }

    //스터디 정보 수정
    @Transactional
    public void modifyStudy(Integer studyId, StudyDtoRequest studyDtoRequest){
        Optional<Study> studyOp = studyRepository.findById(studyId);

        if(studyOp == null) throw new NotFoundException("스터디를 찾을 수 없습니다.");

        Study study = studyOp.get();

        study.updateStudy(studyDtoRequest);

        studyTagRepository.deleteStudyTagByStudy(study);

        List<Integer> tags = studyDtoRequest.getTags();
        for (Integer tag : tags) {
            StudyTagType stt = studyTagTypeRepository.findById(tag).get();
            StudyTag st = new StudyTag(study, stt);
            studyTagRepository.save(st);
        }
    }

    //스터디 가입 신청
    @Transactional
    public Integer addRequest(Integer studyId, RequestDto requestDto, List<MultipartFile> files) {
        Optional<Study> studyOptional = studyRepository.findById(studyId);
        if(studyOptional.isEmpty() || studyOptional.get().getIsDelete()){//존재하지 않는 스터디
            return -3;
        }
        Study study = studyOptional.get();

        Member member = memberRepository.findById(requestDto.getMemberId()).get();

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

        if(files != null) {
            for (MultipartFile file : files) {
                try{
                    String saveFileName = requestDto.getMemberId()+ "_" + String.valueOf(System.currentTimeMillis());
                    fm.upload(file.getInputStream(), saveFileName, file.getContentType(), file.getSize());
                    StudyRequestFile studyRequestFile = new StudyRequestFile(file, studyRequest, saveFileName);
                    studyRequestFileRepository.save(studyRequestFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //스터디 리더에게 스터디 신청 알림을 보내자!
        if(studyRequest.getId()!=null){
            notificationService.sendNotificationToMember(
                    NotificationDto
                            .builder()
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
    public List<RequestDtoResponse> findRequestsByStudy(Integer studyId){
        Study study = studyRepository.findById(studyId).get();
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
    public RequestDtoResponse findRequestById(Integer id){
        StudyRequest request = studyRequestRepository.findStudyRequestById(id).get();
        List<StudyRequestFile> files = request.getStudyRequestFiles();
        List<RequestFile> reponseFiles = new ArrayList<>();
        for (StudyRequestFile file : files) {
            reponseFiles.add(new RequestFile(file));
        }
        return new RequestDtoResponse(request.getId(), new StudyMemberDto(request.getApplicant()), request.getIntroduction(), request.getRequestedAt(), reponseFiles);
    }

    //스터디 신청 파일 다운로드
    public RequestFile requestFileDownload(Integer studyId, Integer requestId, Integer fileId){
        StudyRequestFile studyRequestFile = studyRequestFileRepository.findById(fileId).get();
        byte[] file = null;
        RequestFile result = new RequestFile(studyRequestFile);
        try {
            file = fm.download(studyRequestFile.getSaveFileName());
            result.setFileData(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //가입 신청 승인
    @Transactional
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

    //가입 신청 거절
    @Transactional
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
    public void cancelRequest(Integer requestId, Integer studyId, Integer memberId){
        StudyRequest studyRequest = checkRequest(requestId, studyId, memberId);
        deleteRequest(requestId);
    }

    //스터디 탈퇴
    @Transactional
    public boolean leaveStudy(Integer studyId, Integer memberId){
        Study study = studyRepository.findById(studyId).get();
        if(study.getLeader().getId() == memberId){
            return false;
        }
        studyChatRepository.deleteChatMemberByStudyIdAndAuthorId(studyId, memberId);
        studyMemberRepository.deleteStudyMemberByStudyIdAndMemberId(studyId, memberId);
        return true;
    }

    //스터디원 추방
    @Transactional
    public boolean banMemberStudy(Integer studyId, Integer memberId){
        Study study = studyRepository.findById(studyId).get();
        if(study.getLeader().getId() == memberId){
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
    public void delegateLeader(Integer studyId, Integer leaderId, Integer memberId){
        StudyMember studyLeader,studyMember;
        try {
            if (leaderId == null || memberId == null || leaderId == memberId) throw new updateFailException("잘못된 요청");
            Study study = studyRepository.findById(studyId).get();
            Member leader = study.getLeader();
            if (leader.getId() != leaderId) throw new updateFailException("잘못된 요청");
            Member member = memberRepository.findById(memberId).get();
            study.updateLeader(member);
            studyLeader = studyMemberRepository.findByStudyAndMember(study, leader).get();
            studyLeader.updateLeader(false);
            studyMember = studyMemberRepository.findByStudyAndMember(study, member).get();
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
    public List<StudyMemberDto> findStudyMembers(Integer studyId){
        List<StudyMember> studyMembers = studyMemberRepository.findMembersByStudyId(studyId);
        List<StudyMemberDto> result = new ArrayList<>();
        for (StudyMember studyMember : studyMembers) {
            result.add(new StudyMemberDto(studyMember.getMember(), studyMember.getIsLeader()));
        }
        return result;
    }

    //스터디 실시간 채팅 작성
    @Transactional
    public ChatResponse addChat(Integer studyId, ChatRequest chat){
        Member member = memberRepository.findById(chat.getMemberId()).get();
        Study study = studyRepository.findById(studyId).get();
        if(!checkStudyMember(study.getId(), member.getId())) throw new updateFailException("잘못된 접근");
        StudyChat studyChat = new StudyChat(study, member, chat.getContent());
        studyChatRepository.save(studyChat);
        return new ChatResponse(studyChat);
    }

    //스터디 실시간 채팅 조회
    public List<ChatResponse> findStudyChats(Integer studyId, Integer lastChatId){
        return studyChatRepository.findNewStudyChatsById(studyId, lastChatId == null ? 0 : lastChatId);
    }

    //이전 채팅 보기
    public List<ChatResponse> findOldStudyChats(Integer studyId, Integer startChatId){
        PageRequest pageRequest = PageRequest.of(0, 100);
        return startChatId == null ? studyChatRepository.findOldStudyChats(studyId, pageRequest) : studyChatRepository.findOldStudyChatsByStartId(studyId, startChatId, pageRequest);
    }
    //스터디 일정 조회
    public List<Object> findStudyCalendarsByStudy(Integer studyId){
        Study study = studyRepository.findById(studyId).get();
        List<Object> list = new ArrayList<>();
        List<StudyCalendarDtoResponse> studyCalendar = studyCalendarRepository.findStudyCalendersByStudy(study);
        list.addAll(studyCalendar);
        List<Calendar> memberCalendarEntity = studyCalendarRepository.findMemberCalendarByStudyId(studyId);
        List<CalendarRetrieveResponse> memberCalendar = CalendarListResponse.fromEntity(memberCalendarEntity).getData();
        list.addAll(memberCalendar);
        return list;
    }

    public List<Object> findStudyCalendarsByMemberId(Integer memberId){
        Member member = memberRepository.findMemberById(memberId);
        List<Object> list = new ArrayList<>();
        List<StudyCalendarDtoResponse> studyCalendar = studyCalendarRepository.findStudyCalendersByMemberId(memberId);
        list.addAll(studyCalendar);
        List<Calendar> memberCalendarEntity = calendarRepository.findCalendarsByAuthorId(memberId);
        List<CalendarRetrieveResponse> memberCalendar = CalendarListResponse.fromEntity(memberCalendarEntity).getData();
        list.addAll(memberCalendar);
        return list;
   }

    public List<Object> findStudyCalendarsByMemberIdStudyId(Integer memberId,Integer studyId){
        Member member = memberRepository.findMemberById(memberId);
        List<Object> list = new ArrayList<>();
        List<StudyCalendarDtoResponse> studyCalendar = studyCalendarRepository.findStudyCalendersByMemberIdAndStudyId(memberId,studyId);
        list.addAll(studyCalendar);
        List<Calendar> memberCalendarEntity = calendarRepository.findCalendarsByAuthorId(memberId);
        List<CalendarRetrieveResponse> memberCalendar = CalendarListResponse.fromEntity(memberCalendarEntity).getData();
        list.addAll(memberCalendar);
        return list;
    }

    //일정 개별 조회
    public StudyCalendarDtoResponse findStudyCalendarByStudy(Integer studyId, Integer calendarId){
        Study study = studyRepository.findById(studyId).get();
        return studyCalendarRepository.findStudyCalenderById(calendarId);
    }

    //스터디 일정 추가
    @Transactional
    public Integer addStudyCalendar(Integer studyId, StudyCalendarDtoRequest studyCalendarDtoRequest){
        if(studyCalendarDtoRequest.getStartedAt().isAfter(studyCalendarDtoRequest.getEndedAt())){
            throw new CreationFailException("스터디 일정 시작시간이 끝나는 시간보다 앞서야합니다.");
        }
        Study study = studyRepository.findById(studyId).get();
        Member member = memberRepository.findById(studyCalendarDtoRequest.getMemberId()).get();
        StudyCalendar studyCalendar = new StudyCalendar(study, member, studyCalendarDtoRequest);
        studyCalendarRepository.save(studyCalendar);
        
        //스터디에 일정이 등록될 경우 스터디원들에게 알림을 보냄
        if(studyCalendar.getId()!=null){
            notificationService
                    .sendNotificationToStudyMember(
                            NotificationStudyDto
                                    .builder()
                                    .notificationDto(
                                            NotificationDto
                                                    .builder()
                                                    .notificationType(NotificationType.StudyCalendar)
                                                    .content(study.getTitle()+" 스터디에 일정이 등록되었습니다!")
                                                    .memberId(member.getId())
                                                    .url(studyId.toString())
                                                    .build()
                                    )
                                    .studyId(studyId)
                                    .build()
                    );
        }
        return studyCalendar.getId();
    }


    //스터디 일정 수정
    @Transactional
    public void modifyStudyCalendar(Integer studyId, Integer studyCalendarId, StudyCalendarDtoRequest studyCalendarDtoRequest){
        if(studyCalendarDtoRequest.getStartedAt().isAfter(studyCalendarDtoRequest.getEndedAt())){
            throw new CreationFailException("스터디 일정 시작시간이 끝나는 시간보다 앞서야합니다.");
        }
        StudyCalendar studyCalendar = studyCalendarRepository.findById(studyCalendarId).get();
        studyCalendar.updateCalendar(studyCalendarDtoRequest);
    }


    //스터디 일정 삭제
    @Transactional
    public void removeStudyCalendar(Integer studyId, Integer calendarId){
        studyCalendarRepository.deleteById(calendarId);
    }

    //스터디원인지 체크
    public boolean checkStudyMember(Integer studyId, Integer memberId){
        Optional<StudyMember> result = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId);
        return result.isPresent();
    }

    //스터디의 스터디장인지 체크
    public boolean checkStudyLeader(Integer studyId, Integer memberId){
        Optional<StudyMember> studyMemberOp = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId);
        if(studyMemberOp.isEmpty())
            return false;
        return studyMemberOp.get().getIsLeader() == true ? true : false;
    }

    public StudyMemberDto findStudyMember(Integer studyId, Integer memberId){
        Optional<StudyMember> studyMemberOp = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId);
        if(studyMemberOp.isEmpty())
            return null;
        return new StudyMemberDto(studyMemberOp.get().getMember(), studyMemberOp.get().getIsLeader());
    }

    //스터디 요청 유효성 체크 반환
    public boolean checkStudyRequest(Integer requestId, Integer studyId, Integer memberId){
        return checkRequest(requestId, studyId, memberId) != null;
    }

    //스터디 유효성 체크
    public boolean checkStudy(Integer studyId){
        Optional<Study> study = studyRepository.findById(studyId);
        return study.isPresent() && !study.get().getIsDelete();
    }

    //****************************내부 사용 함수*******************************//


    private Study requestToStudy(StudyDtoRequest studyDtoRequest){
        Study study = new Study(studyDtoRequest);
        return study;
    }

    private void deleteRequest(Integer requestId){
        studyRequestFileRepository.deleteByRequestId(requestId);
        studyRequestRepository.deleteStudyRequestById(requestId);
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

    private void checkExist(Study study){
        if(study == null || study.getIsDelete()) throw new NotFoundException("스터디를 찾을 수 없습니다.");
    }
}
