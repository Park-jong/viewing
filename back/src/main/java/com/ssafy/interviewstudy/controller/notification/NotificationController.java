package com.ssafy.interviewstudy.controller.notification;


import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/users/{userId}/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @JWTRequired(required = true)
    @GetMapping(produces = "text/event-stream")
    public SseEmitter subscribe(@RequestParam(required = false)Integer lastEventId,
                                @MemberInfo JWTMemberInfo jwtMemberInfo){
        System.out.println(jwtMemberInfo);
        return notificationService.connect(jwtMemberInfo.getMemberId(),lastEventId);
    }
}
