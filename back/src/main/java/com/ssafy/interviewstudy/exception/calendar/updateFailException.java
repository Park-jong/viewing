package com.ssafy.interviewstudy.exception.calendar;


import lombok.Getter;

@Getter
public class updateFailException extends RuntimeException{
    private final String target;
    public updateFailException(String target) {
        this.target = target;
    }
}
