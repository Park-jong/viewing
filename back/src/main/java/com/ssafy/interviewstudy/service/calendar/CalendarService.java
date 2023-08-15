package com.ssafy.interviewstudy.service.calendar;

import com.ssafy.interviewstudy.domain.calendar.Calendar;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.calendar.CalendarCreatedResponse;
import com.ssafy.interviewstudy.dto.calendar.CalendarRetrieveRequest;
import com.ssafy.interviewstudy.dto.calendar.CalendarListResponse;
import com.ssafy.interviewstudy.exception.message.CreationFailException;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.calendar.CalendarRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CalendarService {

    private final MemberRepository memberRepository;
    private final CalendarRepository calendarRepository;

    //나의 일정 조회
    @Transactional
    public CalendarListResponse getCalendarList(Integer memberId){
        List<Calendar> calendarList = calendarRepository.findCalendarsByAuthorId(memberId);
        return CalendarListResponse.fromEntity(calendarList);
    }

    //일정 추가
    @Transactional
    public CalendarCreatedResponse createCalendar(CalendarRetrieveRequest calendarDto){
        if(calendarDto.getStartedAt().isAfter(calendarDto.getEndedAt())){
            throw new CreationFailException("일정의 시작시간이 끝나느 시간보다 앞서야 합니다.");
        }
        Member author = memberRepository.findMemberById(calendarDto.getMemberId());
        Calendar calendar = CalendarRetrieveRequest.toEntity(calendarDto,author);
        calendarRepository.save(calendar);
        //Optional을 쓸만한 상황
        if(calendar==null){
            throw new CreationFailException("캘린더");
        }
        if(calendar.getId()==null){
            throw new CreationFailException("캘린더");
        }
        return new CalendarCreatedResponse(calendar.getId());
    }

    //일정 삭제
    @Transactional
    public void deleteCalendar(Integer calendarId){
        Integer result = calendarRepository.deleteCalendarById(calendarId);
        if(result == null){
            throw new NotFoundException("쪽지");
        }
    }

    //일정 수정
    @Transactional
    public void updateCalendar(CalendarRetrieveRequest calendarDto){
        if(calendarDto.getStartedAt().isAfter(calendarDto.getEndedAt())){
            throw new CreationFailException("일정의 시작시간이 끝나는 시간보다 앞서야 합니다.");
        }
        Member updatedMember = memberRepository.findMemberById(calendarDto.getMemberId());
        if(updatedMember==null){
            throw new CreationFailException("캘린더");
        }
        calendarRepository.save(CalendarRetrieveRequest.toEntity(calendarDto,updatedMember));
    }

    //본인 일정이 맞는지 체크
    public Boolean checkOwnCalendar(Integer memberId,Integer calendarId){
        Calendar calendar = calendarRepository.findCalendarByAuthorIdAndId(memberId,calendarId);
        return calendar!=null;
    }

}
