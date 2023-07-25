package com.ssafy.interviewstudy.domain.calendar;

import com.ssafy.interviewstudy.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "calendar")
@EntityListeners(AuditingEntityListener.class)
public class Calendar {
    //일정 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="calendar_id")
    private Integer id;

    @Column(name="started_at",nullable = false)
    LocalDateTime startedAt;

    @Column(name="ended_at",nullable = false)
    LocalDateTime endedAt;

    @Column(name="description",length = 1000)
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member author;

    @Builder
    public Calendar(Integer id, LocalDateTime startedAt, LocalDateTime endedAt, String description, Member author) {
        this.id = id;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.description = description;
        this.author = author;
    }
}
