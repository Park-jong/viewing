package com.ssafy.interviewstudy.domain.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

//멤버 배경 색
@Getter
@AllArgsConstructor
public enum MemberProfileBackground {
        COLOR_ff6767("#ff6767"),
        COLOR_a5ffa5("#a5ffa5"),
        COLOR_a9a9ff("#a9a9ff"),
        COLOR_ff76f6("#ff76f6"),
        COLOR_ffff9e("#ffff9e"),
        COLOR_93ffff("#93ffff"),
        COLOR_9c209c("#9c209c"),
        COLOR_ffa600("#ffa600"),
        COLOR_058e00("#058e00"),
        COLOR_ff00ee("ff00ee");

        private final String value;

    @Override
    public String toString() {
        return value;
    }

    @JsonValue
    public String getValue(){
        return value;
    }


    @JsonCreator
    public static MemberProfileBackground from(String s){
        for(MemberProfileBackground m : MemberProfileBackground.values()){
            if(m.getValue().equals(s)) return m;
        }
        throw new NotFoundException("잘못된 멤버 프로필 배경");
    }
}


