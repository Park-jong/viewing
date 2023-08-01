package com.ssafy.interviewstudy.controller.notification;


import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/users/{userId}/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @GetMapping(produces = "text/event-stream")
    public SseEmitter subscribe(@RequestParam(required = false)Integer lastEventId,
                                @PathVariable("userId") Integer memberId){
        return notificationService.connect(memberId,lastEventId);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Notification)
    @PutMapping("/{notificationId}")
    public ResponseEntity readNotification(@PathVariable("userId") Integer memberId,
                                           @PathVariable("notificationId") Integer notificationId){
        Boolean result = notificationService.checkNotification(notificationId);
        if(!result) return ResponseEntity.badRequest().body("잘못된 알림 ID");
        return ResponseEntity.ok().build();
    }

}
