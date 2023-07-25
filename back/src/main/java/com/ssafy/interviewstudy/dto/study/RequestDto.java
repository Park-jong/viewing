package com.ssafy.interviewstudy.dto.study;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class RequestDto {
    private Integer memberId;
    private String content;
    private List<RequestFile> requestFiles = new ArrayList<>();
}
