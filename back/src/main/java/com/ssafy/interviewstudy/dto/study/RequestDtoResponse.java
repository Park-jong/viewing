package com.ssafy.interviewstudy.dto.study;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class RequestDtoResponse {
    private Integer requestId;
    private Author user;
    private String content;
    private LocalDateTime createdAt;
    private List<RequestFile> requestFiles = new ArrayList<>();

    public RequestDtoResponse(Integer requestId, Author user, String content, LocalDateTime createdAt, List<RequestFile> requestFiles) {
        this.requestId = requestId;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.requestFiles = requestFiles;
    }
}
