package com.ssafy.interviewstudy.domain.member;

//멤버 배경 색
public enum MemberProfileBackground {
        COLOR_ff6767,
        COLOR_a5ffa5,
        COLOR_a9a9ff,
        COLOR_ff76f6,
        COLOR_ffff9e,
        COLOR_93ffff,
        COLOR_9c209c,
        COLOR_ffa600,
        COLOR_058e00,
        COLOR_ff00ee;
    @Override
    public String toString() {
        return '#'+name().substring(6);
    }
}


