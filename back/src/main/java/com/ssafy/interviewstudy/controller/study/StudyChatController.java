package com.ssafy.interviewstudy.controller.study;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.dto.study.ChatErrorResponse;
import com.ssafy.interviewstudy.dto.study.ChatRequest;
import com.ssafy.interviewstudy.dto.study.ChatResponse;
import com.ssafy.interviewstudy.exception.calendar.updateFailException;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.service.study.StudyService;
import com.ssafy.interviewstudy.service.study.studyChat.StudyChatQueueManager;
import com.ssafy.interviewstudy.service.study.studyChat.StudyChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StudyChatController {
    private final StudyChatService studyService;
    private final StudyChatQueueManager studyChatQueueManager;

    //같은 스터디의 채팅은 studyId별 큐(단일 스레드)에서 도착한 순서대로 처리
    @MessageMapping("/chats/studies/{study_id}")
    @SendTo("/topic/{study_id}")
    public ChatResponse studyChatsAdd(@DestinationVariable("study_id") Integer studyId, @Payload ChatRequest chatRequest) throws Exception {
        Future<ChatResponse> future = studyChatQueueManager.submit(studyId, () -> studyService.addChat(studyId, chatRequest));
        try {
            return future.get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            }
            throw e;
        }
    }

    //존재하지 않는 스터디/스터디원으로 채팅을 보낸 경우 등 - 보낸 사람에게만 에러 전달, 세션은 유지
    @MessageExceptionHandler({NotFoundException.class, updateFailException.class})
    @SendToUser("/queue/errors")
    public ChatErrorResponse handleChatException(Exception e){
        log.warn("실시간 채팅 처리 실패: {}", e.getMessage());
        return new ChatErrorResponse(e.getMessage());
    }

    //예상치 못한 오류 - 세션이 끊기지 않도록 마지막 방어선으로 처리
    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public ChatErrorResponse handleUnexpectedException(Exception e){
        log.error("실시간 채팅 처리 중 알 수 없는 오류 발생", e);
        return new ChatErrorResponse("채팅 전송에 실패했습니다.");
    }
}
