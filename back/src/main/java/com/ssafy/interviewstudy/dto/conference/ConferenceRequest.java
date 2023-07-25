package com.ssafy.interviewstudy.dto.conference;

import com.ssafy.interviewstudy.domain.conference.ConferenceRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConferenceRequest {

    private Integer studyId;
    private Integer memberId;
    private Integer conferenceRoomId;

    public ConferenceRequest(Integer studyId, Integer memberId) {
        this.studyId = studyId;
        this.memberId = memberId;
    }

    public ConferenceRequest(Integer conferenceRoomId, Integer studyId, Integer memberId) {
        this.conferenceRoomId = conferenceRoomId;
        this.studyId = studyId;
        this.memberId = memberId;
    }
}
