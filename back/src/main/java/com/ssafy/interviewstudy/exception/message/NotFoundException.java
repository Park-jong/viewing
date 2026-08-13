package com.ssafy.interviewstudy.exception.message;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    private final String target;

    public NotFoundException(String target) {
        super(target);
        this.target = target;
    }
}
