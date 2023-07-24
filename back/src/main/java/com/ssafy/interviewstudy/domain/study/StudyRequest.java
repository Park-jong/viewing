package com.ssafy.interviewstudy.domain.study;

import com.ssafy.interviewstudy.domain.member.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class StudyRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_request_id")
    private Integer id;

    private String introduction;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime requestedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member applicant;

    @OneToMany(mappedBy = "request")
    private List<StudyRequestFile> studyRequestFiles = new ArrayList<>();
}
