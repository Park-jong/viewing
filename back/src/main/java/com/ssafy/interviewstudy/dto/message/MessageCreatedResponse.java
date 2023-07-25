package com.ssafy.interviewstudy.dto.message;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MessageCreatedResponse {

    @NotNull
    private Integer messageId;

    public MessageCreatedResponse(Integer messageId) {
        this.messageId = messageId;
    }
}
