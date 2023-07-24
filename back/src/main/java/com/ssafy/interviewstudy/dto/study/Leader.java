package com.ssafy.interviewstudy.dto.study;

import com.ssafy.interviewstudy.domain.member.Member;

public class Leader {
    private Integer id;

    private String nickname;

    private String background;

    private String character;

    public Leader(Member member){
        if(member == null) return;
        this.id = member.getId();
        this.nickname = member.getNickname();
    }
}
