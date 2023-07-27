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
public class StudyMemberDto {
    private Integer id;

    private String nickname;

    private Boolean isLeader;

    private MemberProfileBackground background;

    private MemberProfileImage character;

    public StudyMemberDto(Integer id, String nickname, MemberProfileBackground background, MemberProfileImage character) {
        this.id = id;
        this.nickname = nickname;
        this.background = background;
        this.character = character;
    }

    public StudyMemberDto(Member member){
        if(member == null) return;
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.background = member.getMemberProfileBackground();
        this.character = member.getMemberProfileImage();
        this.isLeader = false;
    }

    public StudyMemberDto(Member member, Boolean isLeader){
        if(member == null) return;
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.background = member.getMemberProfileBackground();
        this.character = member.getMemberProfileImage();
        this.isLeader = isLeader;
    }
}
