package com.ssafy.interviewstudy.controller.study;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.study.*;
import com.ssafy.interviewstudy.service.study.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/studies")
public class StudyController {
    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService){
        this.studyService = studyService;
    }

    //스터디 조회
    @JWTRequired
    @GetMapping
    public ResponseEntity<?> studyList(@MemberInfo JWTMemberInfo memberInfo, @RequestParam(name = "option", required = false) Boolean option, @RequestParam(name = "appliedCompany", required = false) String appliedCompany, @RequestParam(name = "appliedJob", required = false) String appliedJob, @RequestParam(name = "careerLevel", required = false)CareerLevel careerLevel, @PageableDefault(size = 12)Pageable pageable){
        Page<StudyDtoResponse> page = studyService.findStudiesBySearch(memberInfo, option, appliedCompany, appliedJob, careerLevel, pageable);
        return ResponseEntity.ok().body(page);
    }

    @JWTRequired(required = true)
    @GetMapping("/{study_id}")
    public ResponseEntity<?> studyDetail(@MemberInfo JWTMemberInfo memberInfo, @PathVariable("study_id") int studyId){
        StudyDtoResponse study = studyService.findStudyById(memberInfo, studyId);
        return ResponseEntity.ok().body(study);
    }

    @JWTRequired(required = true)
    @GetMapping("/{study_id}/detail")
    public ResponseEntity<?> studyInfoDetail(@MemberInfo JWTMemberInfo memberInfo, @PathVariable("study_id") int studyId){
        StudyDetailDtoResponse study = studyService.findStudyDetailById(memberInfo, studyId);
        return ResponseEntity.ok().body(study);
    }

    @JWTRequired(required = true)
    @PostMapping
    public ResponseEntity<?> studySave(@Valid @RequestBody StudyDtoRequest study){
        Integer madeStudy = null;
        try{
            madeStudy = studyService.addStudy(study);
        }
        catch(ConstraintViolationException ce){
            return ResponseEntity.internalServerError().body("스터디 생성 실패");
        }
        return ResponseEntity.created(URI.create("/studies/"+madeStudy)).body(madeStudy);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Leader)
    @DeleteMapping("/{study_id}")
    public ResponseEntity<?> studyDelete(@PathVariable("study_id") Integer studyId){
        studyService.removeStudy(studyId);
        return ResponseEntity.ok().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Leader)
    @PutMapping("/{study_id}")
    public ResponseEntity<?> studyModify(@PathVariable("study_id") Integer studyId, @Valid @RequestBody StudyDtoRequest study){
        try {
            studyService.modifyStudy(studyId, study);
        }catch(ConstraintViolationException ce){
            return ResponseEntity.internalServerError().body("스터디 수정 실패");
        }
        return ResponseEntity.created(URI.create("/studies/"+studyId)).build();
    }

    @JWTRequired(required = true)
    @PostMapping("/{study_id}/requests")
    public ResponseEntity<?> studyRequestAdd(@PathVariable("study_id") Integer studyId,     @Valid @RequestBody RequestDto request){
        Integer madeRequest = null;
        try{
            madeRequest = studyService.addRequest(studyId, request);
        }
        catch(ConstraintViolationException ce){
            return ResponseEntity.internalServerError().body("신청 실패");
        }
        if (madeRequest == -1){
            return ResponseEntity.badRequest().body("이미 가입된 스터디입니다.");
        }
        else if (madeRequest == -2){
            return ResponseEntity.badRequest().body("중복된 신청입니다.");
        }
        else if (madeRequest == -3){
            return ResponseEntity.badRequest().body("유효하지 않은 접근");
        }
        return ResponseEntity.created(URI.create("/studies")).body(madeRequest);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Leader)
    @GetMapping("/{study_id}/requests")
    public ResponseEntity<?> studyRequestList(@PathVariable("study_id") Integer studyId){
        List<RequestDtoResponse> response = studyService.findRequestsByStudy(studyId);
        return ResponseEntity.ok().body(response);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Study_Request)
    @GetMapping("/{study_id}/requests/{request_id}")
    public ResponseEntity<?> studyRequest(@PathVariable("study_id") Integer studyId, @PathVariable("request_id") Integer requestId){
        RequestDtoResponse response = studyService.findRequestById(requestId);
        return ResponseEntity.ok().body(response);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Leader)
    @PostMapping("/{study_id}/requests/{request_id}/approval")
    public ResponseEntity<?> requestApproval(@PathVariable("study_id") Integer studyId, @PathVariable("request_id") Integer requestId, @Valid@RequestBody Map map){
        Integer memberId = null;
        if(map.containsKey("user_id")) memberId = (Integer)map.get("user_id");
        studyService.permitRequest(requestId, studyId, memberId);
        return ResponseEntity.ok().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Leader)
    @PostMapping("/{study_id}/requests/{request_id}/denial")
    public ResponseEntity<?> requestDenial(@PathVariable("study_id") Integer studyId, @PathVariable("request_id") Integer requestId, @Valid@RequestBody Map map){
        Integer memberId = null;
        if(map.containsKey("user_id")) memberId = (Integer)map.get("user_id");
        studyService.rejectRequest(requestId, studyId, memberId);
        return ResponseEntity.ok().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Study_Request)
    @DeleteMapping("/{study_id}/requests/{request_id}")
    public ResponseEntity<?> requestCancel(@MemberInfo JWTMemberInfo memberInfo, @PathVariable("study_id") Integer studyId, @PathVariable("request_id") Integer requestId){
        Integer memberId = memberInfo.getMemberId();
        studyService.cancelRequest(requestId, studyId, memberId);
        return ResponseEntity.ok().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Leader)
    @DeleteMapping("/{study_id}/members/{user_id}/ban")
    public ResponseEntity<?> studyMemberBan(@PathVariable("study_id") Integer studyId, @PathVariable("user_id") Integer memberId){
        boolean result = studyService.banMemberStudy(studyId, memberId);
        if(!result)
            return ResponseEntity.badRequest().body("스터디장은 추방할 수 없음");
        return ResponseEntity.ok().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Study_Member)
    @DeleteMapping("/{study_id}/members/{user_id}/exit")
    public ResponseEntity<?> studyMemberExit(@PathVariable("study_id") Integer studyId, @PathVariable("user_id") Integer memberId){
        boolean result = studyService.leaveStudy(studyId, memberId);
        if(!result)
            return ResponseEntity.badRequest().body("스터디장은 탈퇴할 수 없음");
        return ResponseEntity.ok().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Leader)
    @PutMapping("/{study_id}/members/leader")
    public ResponseEntity<?> studyLeaderChange(@PathVariable("study_id") Integer studyId, @Valid@RequestBody Map map){

        Integer leaderId = null;
        Integer memberId = null;
        if(map.containsKey("before_leader_id"))
            leaderId = (Integer)map.get("before_leader_id");
        if(map.containsKey("after_leader_id"))
            memberId = (Integer)map.get("after_leader_id");
        studyService.delegateLeader(studyId, leaderId, memberId);
        return ResponseEntity.ok().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Study_Member)
    @GetMapping("/{study_id}/members")
    public ResponseEntity<?> studyMemberList(@PathVariable("study_id") Integer studyId){
        return ResponseEntity.ok().body(studyService.findStudyMembers(studyId));
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Study_Member)
    @PostMapping("/{study_id}/chats")
    public ResponseEntity<?> studyChatsAdd(@PathVariable("study_id") Integer studyId, @Valid@RequestBody ChatRequest chat){
        studyService.addChat(studyId, chat);
        return ResponseEntity.ok().build();
    }

    //@JWTRequired(required = true)
    //@Authority(authorityType = AuthorityType.Study_Member)
    @GetMapping("/{study_id}/chats")
    public ResponseEntity<?> studyChatList(@PathVariable("study_id") Integer studyId, @RequestParam(name = "startChatId", required = false) Integer startChatId){
        List<ChatResponse> oldStudyChats = studyService.findOldStudyChats(studyId, startChatId);
        Collections.reverse(oldStudyChats);
        return ResponseEntity.ok().body(oldStudyChats);
    }

    @JWTRequired(required = true)
    @GetMapping("/{study_id}/calendars")
    public ResponseEntity<?> studyCalendarList(@MemberInfo JWTMemberInfo memberInfo, @PathVariable("study_id") Integer studyId){
        Integer memberId = memberInfo.getMemberId();
        if(!studyService.checkStudyMember(studyId, memberId)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(studyService.findStudyCalendarsByStudy(studyId));
    }

    @JWTRequired(required = true)
    @GetMapping("/{study_id}/calendars/{calendar_id}")
    public ResponseEntity<?> studyCalendarDetail(@MemberInfo JWTMemberInfo memberInfo, @PathVariable("study_id") Integer studyId, @PathVariable("calendar_id") Integer calendarId){
        Integer memberId = memberInfo.getMemberId();
        if(!studyService.checkStudyMember(studyId, memberId)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(studyService.findStudyCalendarByStudy(studyId, calendarId));
    }

    @JWTRequired(required = true)
    @PostMapping("/{study_id}/calendars")
    public ResponseEntity<?> studyCalendarAdd(@MemberInfo JWTMemberInfo memberInfo, @PathVariable("study_id") Integer studyId, @Valid @RequestBody StudyCalendarDtoRequest studyCalendar){
        Integer memberId = memberInfo.getMemberId();
        if(!studyService.checkStudyMember(studyId, memberId)) return ResponseEntity.badRequest().build();
        Integer madeSchedule = null;
        try {
            madeSchedule = studyService.addStudyCalendar(studyId, studyCalendar);
        }
        catch(ConstraintViolationException ce){
            return ResponseEntity.internalServerError().body("일정 생성 실패");
        }
        return ResponseEntity.ok().body(madeSchedule);
    }

    @JWTRequired(required = true)
    @PutMapping("/{study_id}/calendars/{calendar_id}")
    public ResponseEntity<?> studyCalendarModify(@MemberInfo JWTMemberInfo memberInfo, @PathVariable("study_id") Integer studyId, @PathVariable("calendar_id") Integer calendarId, @Valid @RequestBody StudyCalendarDtoRequest studyCalendar){
        Integer memberId = memberInfo.getMemberId();
        if(!studyService.checkStudyMember(studyId, memberId)) return ResponseEntity.badRequest().build();
        studyService.modifyStudyCalendar(studyId, calendarId, studyCalendar);
        return ResponseEntity.ok().build();
    }

    @JWTRequired(required = true)
    @DeleteMapping("/{study_id}/calendars/{calendar_id}")
    public ResponseEntity<?> studyCalendarRemove(@MemberInfo JWTMemberInfo memberInfo, @PathVariable("study_id") Integer studyId, @PathVariable("calendar_id") Integer calendarId){
        Integer memberId = memberInfo.getMemberId();
        if(!studyService.checkStudyMember(studyId, memberId)) return ResponseEntity.badRequest().build();
        studyService.removeStudyCalendar(studyId, calendarId);
        return ResponseEntity.ok().build();
    }

}
