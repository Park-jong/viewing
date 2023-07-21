package com.ssafy.interviewstudy.domain.study;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRequestFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_request_file_id")
    private Integer id;

    private String originalFileName;

    private String file;

    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_request_id")
    private StudyRequest request;
}
