package com.ssafy.interviewstudy.service.study.studyCalendar;

import com.ssafy.interviewstudy.domain.calendar.Calendar;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.dto.calendar.CalendarListResponse;
import com.ssafy.interviewstudy.dto.calendar.CalendarRetrieveResponse;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.dto.notification.NotificationStudyDto;
import com.ssafy.interviewstudy.dto.study.*;
import com.ssafy.interviewstudy.exception.message.CreationFailException;
import com.ssafy.interviewstudy.exception.study.StudyExceptionFactory;
import com.ssafy.interviewstudy.repository.calendar.CalendarRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.*;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyCalendarServiceImpl implements StudyCalendarService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyCalendarRepository studyCalendarRepository;
    private final NotificationService notificationService;
    private final CalendarRepository calendarRepository;

    //스터디 일정 조회
    @Override
    public List<Object> findStudyCalendarsByStudy(Integer studyId){
        Study study = studyRepository.findById(studyId).orElseThrow(StudyExceptionFactory::studyNotFound);
        List<StudyCalendarDtoResponse> studyCalendar = studyCalendarRepository.findStudyCalendersByStudy(study);
        List<Object> list = new ArrayList<>(studyCalendar);
        List<Calendar> memberCalendarEntity = studyCalendarRepository.findMemberCalendarByStudyId(studyId);
        List<CalendarRetrieveResponse> memberCalendar = CalendarListResponse.fromEntity(memberCalendarEntity).getData();
        list.addAll(memberCalendar);
        return list;
    }

    @Override
    public List<Object> findStudyCalendarsByMemberId(Integer memberId){
        Member member = memberRepository.findMemberById(memberId).orElseThrow(StudyExceptionFactory::studyMemberNotFound);
        List<StudyCalendarDtoResponse> studyCalendar = studyCalendarRepository.findStudyCalendersByMemberId(memberId);
        List<Object> list = new ArrayList<>(studyCalendar);
        List<Calendar> memberCalendarEntity = calendarRepository.findCalendarsByAuthorId(memberId);
        List<CalendarRetrieveResponse> memberCalendar = CalendarListResponse.fromEntity(memberCalendarEntity).getData();
        list.addAll(memberCalendar);
        return list;
   }

    @Override
    public List<Object> findStudyCalendarsByMemberIdStudyId(Integer memberId, Integer studyId){
        Member member = memberRepository.findMemberById(memberId).orElseThrow(StudyExceptionFactory::studyMemberNotFound);
        List<StudyCalendarDtoResponse> studyCalendar = studyCalendarRepository.findStudyCalendersByMemberIdAndStudyId(memberId,studyId);
        List<Object> list = new ArrayList<>(studyCalendar);
        List<Calendar> memberCalendarEntity = calendarRepository.findCalendarsByAuthorId(memberId);
        List<CalendarRetrieveResponse> memberCalendar = CalendarListResponse.fromEntity(memberCalendarEntity).getData();
        list.addAll(memberCalendar);
        return list;
    }

    //일정 개별 조회
    @Override
    public StudyCalendarDtoResponse findStudyCalendarByStudy(Integer studyId, Integer calendarId){
        Study study = studyRepository.findById(studyId).orElseThrow(StudyExceptionFactory::studyNotFound);
        return studyCalendarRepository.findStudyCalenderById(calendarId);
    }

    //스터디 일정 추가
    @Transactional
    @Override
    public Integer addStudyCalendar(Integer studyId, StudyCalendarDtoRequest studyCalendarDtoRequest){
        if(studyCalendarDtoRequest.getStartedAt().isAfter(studyCalendarDtoRequest.getEndedAt())){
            throw new CreationFailException("스터디 일정 시작시간이 끝나는 시간보다 앞서야합니다.");
        }
        Study study = studyRepository.findById(studyId).orElseThrow(StudyExceptionFactory::studyNotFound);
        Member member = memberRepository.findById(studyCalendarDtoRequest.getMemberId()).orElseThrow(StudyExceptionFactory::studyMemberNotFound);
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
    @Override
    public void modifyStudyCalendar(Integer studyId, Integer studyCalendarId, StudyCalendarDtoRequest studyCalendarDtoRequest){
        if(studyCalendarDtoRequest.getStartedAt().isAfter(studyCalendarDtoRequest.getEndedAt())){
            throw new CreationFailException("스터디 일정 시작시간이 끝나는 시간보다 앞서야합니다.");
        }
        StudyCalendar studyCalendar = studyCalendarRepository.findById(studyCalendarId).orElseThrow(StudyExceptionFactory::studyCalendarNotFound);
        studyCalendar.updateCalendar(studyCalendarDtoRequest);
    }

    //스터디 일정 삭제
    @Transactional
    @Override
    public void removeStudyCalendar(Integer studyId, Integer calendarId){
        studyCalendarRepository.deleteById(calendarId);
    }
}
