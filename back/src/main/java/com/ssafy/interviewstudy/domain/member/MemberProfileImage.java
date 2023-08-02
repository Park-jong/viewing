package com.ssafy.interviewstudy.domain.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ssafy.interviewstudy.exception.message.CreationFailException;

//멤버 프로필 캐릭터
public enum MemberProfileImage {
    cow,crab,dolphin,jellyfish,koala,
    octopus,penguin,seahorse,sheep,turtle;

    private final String prefix = "/profile/";
    private final String ext = ".png";


    @JsonValue
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name());
        return sb.toString();
    }

    @JsonCreator
    public static MemberProfileImage from(String s){
        for(MemberProfileImage m : MemberProfileImage.values()){
            if(m.toString().equals(s)){
                return m;
            }
        }
        throw new CreationFailException("잘못된 캐릭터 이미지");
    }
}
