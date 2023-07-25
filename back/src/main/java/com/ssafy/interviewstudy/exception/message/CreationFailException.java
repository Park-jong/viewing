package com.ssafy.interviewstudy.exception.message;

import lombok.Getter;

@Getter
public class CreationFailException extends RuntimeException{
    public String creation;
    public CreationFailException(String creation) {
        this.creation = creation;
    }
}
