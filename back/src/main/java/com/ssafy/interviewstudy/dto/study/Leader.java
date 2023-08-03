package com.ssafy.interviewstudy.dto.study;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.member.MemberProfileBackground;
import com.ssafy.interviewstudy.domain.member.MemberProfileImage;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Leader {
    private Integer memberId;

    private String nickname;

    private MemberProfileBackground background;

    private MemberProfileImage character;

    public Leader(Member member){
        if(member == null) return;
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.background = member.getMemberProfileBackground();
        this.character = member.getMemberProfileImage();
    }
}
