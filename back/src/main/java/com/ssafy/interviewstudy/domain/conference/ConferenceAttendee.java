package com.ssafy.interviewstudy.domain.conference;

import com.ssafy.interviewstudy.domain.member.Member;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "conference_attendee")
public class ConferenceAttendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conference_attendee_id")
    private Integer id;

    @JoinColumn(name = "conference_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ConferenceRoom conferenceRoom;

    @JoinColumn(name = "attendee_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member attendee;

    public ConferenceAttendee(ConferenceRoom conferenceRoom, Member attendee) {
        this.conferenceRoom = conferenceRoom;
        this.attendee = attendee;
    }
}
