package com.ssafy.interviewstudy.controller.message;

import com.ssafy.interviewstudy.dto.message.MessageDto;
import com.ssafy.interviewstudy.dto.message.MessageListResponse;
import com.ssafy.interviewstudy.dto.message.MessageSendRequest;
import com.ssafy.interviewstudy.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

//메세지를 담당하는 컨트롤러
@RequestMapping(value={"/users/{userId}/messages","/messages"})
@RestController
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    
    //보낸 쪽지함
    @GetMapping("/send")
    public ResponseEntity<?> getSentMessage(@PathVariable Integer userId){
        MessageListResponse messageList = messageService.getSentMessages(userId);
        return ResponseEntity.ok().body(messageList);
    }

    //받은 쪽지함
    @GetMapping("/receive")
    public ResponseEntity<?> getReceivedMessage(@PathVariable Integer userId){
        MessageListResponse messageList = messageService.getReceivedMessages(userId);
        return ResponseEntity.ok().body(messageList);
    }

    //쪽지 조회
    @GetMapping("/{messageId}")
    public ResponseEntity<?> getMessageDetail(@PathVariable Integer messageId){
        MessageDto messageDto = messageService.getMessageDetail(messageId);
        return ResponseEntity.ok(messageDto);
    }

    //쪽지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId){
        Integer result = messageService.deleteMessage(messageId);
        System.out.println(result);
        return ResponseEntity.ok().build();
    }

    //쪽지 보내기
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody MessageSendRequest messageSendRequest){
        Integer messageId = messageService.sendMessage(messageSendRequest);
        if(messageId==null) return ResponseEntity.internalServerError().build();
        return ResponseEntity.created(URI.create("/messages/"+messageId)).build();
    }
}
