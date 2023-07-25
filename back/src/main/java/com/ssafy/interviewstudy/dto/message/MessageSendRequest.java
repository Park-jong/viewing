package com.ssafy.interviewstudy.dto.message;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.message.Message;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class MessageSendRequest {

    @NotNull
    //쪽지 보내는 사람
    private Integer authorId;

    @NotNull
    //쪽지 받는사람
    private Integer receiverId;

    //쪽지 내용
    private String content;

    //쪽지 제목
    private String title;

    public static Message toEntity(MessageSendRequest messageSendRequest,Member author,Member receiver){
        Message message =
                Message.builder()
                        .title(messageSendRequest.getTitle())
                        .content(messageSendRequest.getContent())
                        .author(author)
                        .receiver(receiver)
                        .build();
        return message;
    }
}
