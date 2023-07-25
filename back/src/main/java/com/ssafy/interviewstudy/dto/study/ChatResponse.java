package com.ssafy.interviewstudy.dto.study;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class ChatResponse {
    private Integer chatId;
    private Author author;
    private String content;
    private LocalDateTime createdAt;
}
