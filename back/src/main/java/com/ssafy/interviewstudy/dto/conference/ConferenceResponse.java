package com.ssafy.interviewstudy.dto.conference;

import com.ssafy.interviewstudy.domain.conference.ConferenceAttendee;
import com.ssafy.interviewstudy.domain.conference.ConferenceRoom;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.member.MemberProfileBackground;
import com.ssafy.interviewstudy.domain.member.MemberProfileImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ConferenceResponse {

    static class conferenceMember {
        private Integer id;
        private Integer attendeeId;
        private String nickname;
        private MemberProfileImage memberProfileImage;
        private MemberProfileBackground memberProfileBackground;

        public conferenceMember(ConferenceAttendee conferenceAttendee) {
            Member member = conferenceAttendee.getAttendee();
            this.id = member.getId();
            this.attendeeId = conferenceAttendee.getId();
            this.nickname = member.getNickname();
            this.memberProfileImage = member.getMemberProfileImage();
            this.memberProfileBackground = member.getMemberProfileBackground();
        }
    }

    private Integer conferenceRoomId;
    private List<conferenceMember> conferenceMemberList;
    private String sessionId;
    private String token;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setToken(String Token) {
        this.token = token;
    }

    public ConferenceResponse(ConferenceRoom conferenceRoom) {
        this.conferenceRoomId = conferenceRoom.getId();
        this.conferenceMemberList =
                conferenceRoom.getConferenceAttendeeList().stream()
                        .map((ConferenceAttendee ca) -> new conferenceMember(ca))
                        .collect(Collectors.toList());
    }

}
