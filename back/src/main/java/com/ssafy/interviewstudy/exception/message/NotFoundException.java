package com.ssafy.interviewstudy.exception.message;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    private String target;

    public NotFoundException(String target) {
        this.target = target;
    }
}
