package com.ssafy.interviewstudy.controller.study;

import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.domain.study.StudyCalendar;
import com.ssafy.interviewstudy.dto.message.MessageCreatedResponse;
import com.ssafy.interviewstudy.dto.message.MessageSendRequest;
import com.ssafy.interviewstudy.dto.study.*;
import com.ssafy.interviewstudy.service.study.StudyService;
import org.apache.coyote.Response;
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
    @GetMapping
    public ResponseEntity<?> studyList(@Param("option") Boolean option, @Param("appliedCompany") Integer appliedCompany, @Param("appliedJob") String appliedJob, @Param("careerLevel")CareerLevel careerLevel, @PageableDefault(size = 20)Pageable pageable){
        Page<StudyDtoResponse> page = studyService.findStudiesBySearch(option, appliedCompany, appliedJob, careerLevel, pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/{study_id}")
    public ResponseEntity<?> studyDetail(@PathVariable("study_id") int studyId){
        StudyDtoResponse study = studyService.findStudyById(studyId);
        return ResponseEntity.ok().body(study);
    }

    @PostMapping
    public ResponseEntity<?> studySave(@Valid @RequestBody StudyDtoRequest study){
        Integer madeStudy = null;
        try{
            madeStudy = studyService.addStudy(study);
        }
        catch(ConstraintViolationException ce){
            return ResponseEntity.internalServerError().body("스터디 생성 실패");
        }
        return ResponseEntity.created(URI.create("/studies/"+madeStudy)).build();
    }

    @DeleteMapping("/{study_id}")
    public ResponseEntity<?> studyDelete(@PathVariable("study_id") Integer studyId){
        studyService.removeStudy(studyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{study_id}")
    public ResponseEntity<?> studyModify(@PathVariable("study_id") Integer studyId, @Valid @RequestBody StudyDtoRequest study){
        try {
            studyService.modifyStudy(studyId, study);
        }catch(ConstraintViolationException ce){
            return ResponseEntity.internalServerError().body("스터디 수정 실패");
        }
        return ResponseEntity.created(URI.create("/studies/"+studyId)).build();
    }

    @PostMapping("/{study_id}/requests")
    public ResponseEntity<?> studyRequestAdd(@PathVariable("study_id") Integer studyId, @Valid @RequestBody RequestDto request){
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
        return ResponseEntity.created(URI.create("/studies/"+madeRequest)).build();
    }

    @GetMapping("/{study_id}/requests")
    public ResponseEntity<?> studyRequestList(@PathVariable("study_id") Integer studyId){
        List<RequestDtoResponse> response = studyService.findRequestsByStudy(studyId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{study_id}/requests/{request_id}")
    public ResponseEntity<?> studyRequest(@PathVariable("study_id") Integer studyId, @PathVariable("request_id") Integer requestId){
        RequestDtoResponse response = studyService.findRequestById(requestId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{study_id}/requests/{request_id}/approval")
    public ResponseEntity<?> requestApproval(@PathVariable("study_id") Integer studyId, @PathVariable("request_id") Integer requestId, @Valid@RequestBody Map map){
        Integer userId = null;
        if(map.containsKey("user_id")) userId = (Integer)map.get("user_id");
        studyService.permitRequest(requestId, studyId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{study_id}/requests/{request_id}/denial")
    public ResponseEntity<?> requestDenial(@PathVariable("study_id") Integer studyId, @PathVariable("request_id") Integer requestId, @Valid@RequestBody Map map){
        Integer userId = null;
        if(map.containsKey("user_id")) userId = (Integer)map.get("user_id");
        studyService.rejectRequest(requestId, studyId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{study_id}/members/{user_id}")
    public ResponseEntity<?> studyMemberBan(@PathVariable("study_id") Integer studyId, @PathVariable("user_id") Integer memberId){
        boolean result = studyService.banMemberStudy(studyId, memberId);
        if(!result)
            return ResponseEntity.badRequest().body("스터디장은 추방할 수 없음");
        return ResponseEntity.ok().build();
    }

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

    @GetMapping("/{study_id}/members")
    public ResponseEntity<?> studyMemberList(@PathVariable("study_id") Integer studyId){
        return ResponseEntity.ok().body(studyService.findStudyMembers(studyId));
    }

    @PostMapping("/{study_id}/chats")
    public ResponseEntity<?> studyChatsAdd(@PathVariable("study_id") Integer studyId, @Valid@RequestBody ChatRequest chat){
        studyService.addChat(studyId, chat);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{study_id}/chats")
    public ResponseEntity<?> studyChatList(@PathVariable("study_id") Integer studyId, @Param("lastChatId") Integer lastChatId){
        return ResponseEntity.ok().body(studyService.findStudyChats(studyId, lastChatId));
    }

    @GetMapping("/{study_id}/calendars")
    public ResponseEntity<?> studyCalendarList(@PathVariable("study_id") Integer studyId){
        return ResponseEntity.ok().body(studyService.findStudyCalenarByStudy(studyId));
    }

    @GetMapping("/{study_id}/calendars/{calendar_id}")
    public ResponseEntity<?> studyCalendarDetail(@PathVariable("study_id") Integer studyId, @PathVariable("calendar_id") Integer calendarId){
        return ResponseEntity.ok().body(studyService.findStudyCalenarByStudy(studyId, calendarId));
    }

    @PostMapping("/{study_id}/calendars")
    public ResponseEntity<?> studyCalendarAdd(@PathVariable("study_id") Integer studyId, @Valid @RequestBody StudyCalendarDtoRequest studyCalendar){
        studyService.addStudyCalendar(studyId, studyCalendar);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{study_id}/calendars/{calendar_id}")
    public ResponseEntity<?> studyCalendarModify(@PathVariable("study_id") Integer studyId, @PathVariable("calendar_id") Integer calendarId, @Valid @RequestBody StudyCalendarDtoRequest studyCalendar){
        studyService.modifyStudyCalendar(studyId, calendarId, studyCalendar);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{study_id}/calendars/{calendar_id}")
    public ResponseEntity<?> studyCalendarRemove(@PathVariable("study_id") Integer studyId, @PathVariable("calendar_id") Integer calendarId){
        studyService.removeStudyCalendar(studyId, calendarId);
        return ResponseEntity.ok().build();
    }

}
