package com.ssafy.interviewstudy.domain.conference;

import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.dto.conference.ConferenceRequest;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "conference_room")
@EntityListeners(AuditingEntityListener.class)
public class ConferenceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="conference_room_id")
    private Integer id;


    @JoinColumn(name = "study_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Study study;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "conferenceRoom", cascade = CascadeType.REMOVE)
    private List<ConferenceAttendee> conferenceAttendeeList = new ArrayList<>();

    public ConferenceRoom(Study study) {
        this.study = study;
    }
}
