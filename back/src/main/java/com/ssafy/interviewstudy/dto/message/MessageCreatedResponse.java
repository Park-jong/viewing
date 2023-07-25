package com.ssafy.interviewstudy.dto.message;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MessageCreatedResponse {

    @NotNull
    private Integer messageId;

    public MessageCreatedResponse(Integer messageId) {
        this.messageId = messageId;
    }
}
