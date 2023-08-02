package com.ssafy.interviewstudy.dto.board;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.member.MemberProfileBackground;
import com.ssafy.interviewstudy.domain.member.MemberProfileImage;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Author {
    private Integer memberId;
    private String nickname;
    private MemberProfileBackground memberProfileBackground;
    private MemberProfileImage memberProfileImage;

//    public Author(Integer memberId, String nickname, MemberProfileBackground memberProfileBackground, MemberProfileImage memberProfileImage) {
//        this.memberId = memberId;
//        this.nickname = nickname;
//        this.memberProfileBackground = memberProfileBackground;
//        this.memberProfileImage = memberProfileImage;
//    }

    public Author(Member member) {
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.memberProfileBackground = member.getMemberProfileBackground();
        this.memberProfileImage = member.getMemberProfileImage();
    }

}
