package com.ssafy.interviewstudy.dto.common;

import com.ssafy.interviewstudy.domain.member.MemberProfileBackground;
import com.ssafy.interviewstudy.domain.member.MemberProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class memberDto {
    Integer id;
    String nickname;
    MemberProfileBackground background;
    MemberProfileImage character;

    @Builder
    public memberDto(Integer id, String nickname, MemberProfileBackground background, MemberProfileImage character) {
        this.id = id;
        this.nickname = nickname;
        this.background = background;
        this.character = character;
    }
}
