package com.ssafy.interviewstudy.controller.study;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.dto.study.ChatRequest;
import com.ssafy.interviewstudy.dto.study.ChatResponse;
import com.ssafy.interviewstudy.service.study.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class StudyChatController {
    private final StudyService studyService;

//    @JWTRequired(required = true)
//    @Authority(authorityType = AuthorityType.Study_Member)
//    @MessageMapping("/chats")
//    @SendTo("/app")
//    public ResponseEntity<?> studyChatsAdd(@PathVariable("study_id") Integer studyId, @Valid @RequestBody ChatRequest chat){
//        studyService.addChat(studyId, chat);
//        return ResponseEntity.ok().build();
//    }

//    @JWTRequired(required = true)
//    @Authority(authorityType = AuthorityType.Study_Member)
//    @GetMapping("/chats")
//    @SendTo("/topic")
//    public ResponseEntity<?> studyChatList(@PathVariable("study_id") Integer studyId, @RequestParam(name = "lastChatId", required = false) Integer lastChatId){
//        return ResponseEntity.ok().body(studyService.findStudyChats(studyId, lastChatId));
//    }

    @MessageMapping("/chats/studies/{study_id}")
    @SendTo("/topic/{study_id}")
    public ChatResponse studyChatsAdd(@DestinationVariable("study_id") Integer studyId){
        return ChatResponse.builder().build();
    }
}
