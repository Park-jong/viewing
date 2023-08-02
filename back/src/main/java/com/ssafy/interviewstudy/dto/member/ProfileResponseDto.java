package com.ssafy.interviewstudy.dto.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.member.MemberProfileBackground;
import com.ssafy.interviewstudy.domain.member.MemberProfileImage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileResponseDto {

    private Integer userId;

    private String nickname;

    private MemberProfileBackground backgroundColor;

    private MemberProfileImage backgroundImg;

    public ProfileResponseDto(Member member){
        this.userId = member.getId();
        this.nickname = member.getNickname();
        this.backgroundColor = member.getMemberProfileBackground();
        this.backgroundImg = member.getMemberProfileImage();
    }
}
