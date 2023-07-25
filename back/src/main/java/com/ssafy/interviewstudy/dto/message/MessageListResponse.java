package com.ssafy.interviewstudy.dto.message;


import com.ssafy.interviewstudy.domain.message.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//메시지를 목록으로 보여주는 응답
@Getter
@NoArgsConstructor
public class MessageListResponse {

    private List<MessageDto> data = new ArrayList<>();

    public static MessageListResponse fromEntityList(List<Message> messageList){
        MessageListResponse messageListResponse = new MessageListResponse();
        for(Message m : messageList){
            messageListResponse.getData().
                    add(MessageDto.fromEntityWithoutContent(m));
        }
        return messageListResponse;
    }
}
