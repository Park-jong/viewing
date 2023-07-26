package com.ssafy.interviewstudy.dto.study;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.member.MemberProfileBackground;
import com.ssafy.interviewstudy.domain.member.MemberProfileImage;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ChatResponse {
    private Integer chatId;
    private StudyMemberDto user;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;

    public ChatResponse(Integer chatId, Integer userId, String userNickname, MemberProfileBackground userBg, MemberProfileImage userCharcter, String content, LocalDateTime createdAt){
        this.chatId = chatId;
        this.content = content;
        this.createdAt = createdAt;
        this.user = new StudyMemberDto(userId, userNickname, userBg, userCharcter);
    }

    public ChatResponse(Integer chatId){
        this.chatId = chatId;
    }
}
