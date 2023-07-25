package com.ssafy.interviewstudy.domain.member;

//멤버 프로필 캐릭터
public enum MemberProfileImage {
    cow,crab,dolphin,jellyfish,koala,
    octopus,penguin,seahorse,sheep,turtle;

    private final String prefix = "/profile/";
    private final String ext = ".png";


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(name());
        sb.append(ext);
        return sb.toString();
    }
}
