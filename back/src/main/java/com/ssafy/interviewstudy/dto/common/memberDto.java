package com.ssafy.interviewstudy.dto.common;

import com.ssafy.interviewstudy.domain.member.MemberProfileBackground;
import com.ssafy.interviewstudy.domain.member.MemberProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class memberDto {
    Integer memberId;
    String nickname;
    MemberProfileBackground background;
    MemberProfileImage character;

    @Builder
    public memberDto(Integer memberId, String nickname, MemberProfileBackground background, MemberProfileImage character) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.background = background;
        this.character = character;
    }
}
