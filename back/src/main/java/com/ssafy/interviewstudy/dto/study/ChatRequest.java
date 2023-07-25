package com.ssafy.interviewstudy.dto.study;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class ChatRequest {
    private Integer userId;
    private String content;
}
