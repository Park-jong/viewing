package com.ssafy.interviewstudy.controller.message;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.message.MessageCreatedResponse;
import com.ssafy.interviewstudy.dto.message.MessageDto;
import com.ssafy.interviewstudy.dto.message.MessageListResponse;
import com.ssafy.interviewstudy.dto.message.MessageSendRequest;
import com.ssafy.interviewstudy.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

//메세지를 담당하는 컨트롤러
@RequestMapping(value={"/members/{memberId}/messages","/messages"})
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;



    //보낸 쪽지함
    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @GetMapping("/send")
    public ResponseEntity<?> getSentMessage(@PathVariable Integer memberId){
        MessageListResponse messageList = messageService.getSentMessages(memberId);
        return ResponseEntity.ok().body(messageList);
    }

    //받은 쪽지함
    @JWTRequired
    @Authority(authorityType = AuthorityType.Member)
    @GetMapping("/receive")
    public ResponseEntity<?> getReceivedMessage(@PathVariable Integer memberId){
        MessageListResponse messageList = messageService.getReceivedMessages(memberId);
        return ResponseEntity.ok().body(messageList);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Message)
    //쪽지 조회
    @GetMapping("/{messageId}")
    public ResponseEntity<?> getMessageDetail(@PathVariable Integer messageId){
        MessageDto messageDto = messageService.getMessageDetail(messageId);
        return ResponseEntity.ok(messageDto);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Message)
    //쪽지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId){
        Integer result = messageService.deleteMessage(messageId);
        System.out.println(result);
        return ResponseEntity.ok().build();
    }

    @JWTRequired(required = true)
    //쪽지 보내기
    @PostMapping
    public ResponseEntity<?> sendMessage(@MemberInfo JWTMemberInfo memberInfo,
                                         @Valid @RequestBody MessageSendRequest messageSendRequest){
        messageSendRequest.setAuthorId(memberInfo.getMemberId());
        MessageCreatedResponse messageCreatedResponse;
        try{
            messageCreatedResponse = messageService.sendMessage(messageSendRequest);
        }
        catch(ConstraintViolationException ce){
            //쪽지 생성 실패
            return ResponseEntity.internalServerError().body("쪽지 보내기 실패");
        }
       return ResponseEntity.created(URI.create("/messages/"+messageCreatedResponse.getMessageId()))
               .body(messageCreatedResponse);
    }
}
