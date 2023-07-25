package com.ssafy.interviewstudy.dto.message;


import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.message.Message;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class MessageDto {

    //메세지 PK
    private Integer messageId;

    //메세지 제목
    private String title;

    //메세지 내용
    private String content;
    
    //메세지 생성일 (보낸 시간)
    private LocalDateTime createdAt;

    //수신 여부
    //쪽지는 읽는 순간 수신? 안수신?
    private Boolean isRead;

    //메세지 쓴 사람
    private messageMember author;

    //메세지 받은 사람
    private messageMember receiver;

    //엔티티로 부터 메시지 조회 응답 dto 생성
    public static MessageDto fromEntityWithoutContent(Message message){
        MessageDto messageDetailResponse = new MessageDto();

        messageDetailResponse.setMessageId(message.getId());
        messageDetailResponse.setTitle(message.getTitle());
        messageDetailResponse.setCreatedAt(message.getCreatedAt());
        messageDetailResponse.setIsRead(message.getIsRead());
        messageDetailResponse.setAuthor(
                messageMember.builder()
                        .id(message.getAuthor().getId())
                        .background(message.getAuthor().getMemberProfileBackground())
                        .character(message.getAuthor().getMemberProfileImage())
                        .build()
        );
        messageDetailResponse.setReceiver(
                messageMember.builder()
                        .id(message.getReceiver().getId())
                        .background(message.getReceiver().getMemberProfileBackground())
                        .character(message.getReceiver().getMemberProfileImage())
                        .build()
        );
        return messageDetailResponse;
    }

    public static MessageDto fromEntity(Message message){
        MessageDto messageDetailResponse = fromEntityWithoutContent(message);
        messageDetailResponse.setContent(message.getContent());
        return messageDetailResponse;
    }
}
