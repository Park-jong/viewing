package com.ssafy.interviewstudy.dto.study;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RequestDtoResponse {
    private Integer requestId;
    private StudyMemberDto user;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;
    private List<RequestFile> requestFiles = new ArrayList<>();

    public RequestDtoResponse(Integer requestId, StudyMemberDto user, String content, LocalDateTime createdAt, List<RequestFile> requestFiles) {
        this.requestId = requestId;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.requestFiles = requestFiles;
    }

    public RequestDtoResponse(Integer requestId, StudyMemberDto user, String content, LocalDateTime createdAt) {
        this.requestId = requestId;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
    }
}
