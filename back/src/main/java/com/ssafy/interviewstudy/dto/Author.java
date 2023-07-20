package com.ssafy.interviewstudy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private int id;
    private String nickname;

    private byte[] background;
    private byte[] avatar;
    private byte[] hat;
}
