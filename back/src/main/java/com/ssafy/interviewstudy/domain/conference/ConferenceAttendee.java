package com.ssafy.interviewstudy.domain.conference;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "conference_attendee")
public class ConferenceAttendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "conference_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ConferenceRoom conferenceRoom;

//    @JoinColumn(name = "attendee_id", nullable = false)
//    @OneToOne(fetch = FetchType.LAZY)
//    private User attendee;
}
