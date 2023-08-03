package com.ssafy.interviewstudy.dto.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.member.MemberProfileBackground;
import com.ssafy.interviewstudy.domain.member.MemberProfileImage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileResponseDto {

    private Integer memberId;

    private String nickname;

    private MemberProfileBackground backgroundColor;

    private MemberProfileImage backgroundImg;

    public ProfileResponseDto(Member member){
        this.memberId = member.getId();
        this.nickname = member.getNickname();
        this.backgroundColor = member.getMemberProfileBackground();
        this.backgroundImg = member.getMemberProfileImage();
    }
}
