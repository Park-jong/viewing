package com.ssafy.interviewstudy.dto.member.jwt;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JWTToken {
    private String accessToken;

    @Builder
    public JWTToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
