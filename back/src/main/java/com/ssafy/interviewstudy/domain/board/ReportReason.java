package com.ssafy.interviewstudy.domain.board;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "report_reason")
public class ReportReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private ReportArticle reportArticle;

    @Column(name = "description", nullable = false)
    private String description;
}
