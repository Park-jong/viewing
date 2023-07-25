package com.ssafy.interviewstudy.dto.conference;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConferenceExitRequest {
    private Integer attendeeId;
    private Integer conferenceRoomId;

    public ConferenceExitRequest(Integer attendeeId, Integer conferenceRoomId) {
        this.attendeeId = attendeeId;
        this.conferenceRoomId = conferenceRoomId;
    }
}
