package com.ssafy.interviewstudy.dto.study;

import com.ssafy.interviewstudy.domain.member.Member;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class Author {
    private Integer id;

    private String nickname;

    private String background;

    private String character;

    public Author(Member member){
        if(member == null) return;
        this.id = member.getId();
        this.nickname = member.getNickname();
    }
}
